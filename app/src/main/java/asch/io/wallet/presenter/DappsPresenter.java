package asch.io.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asch.io.base.adapter.page.IPage;
import asch.io.base.adapter.page.Page1;
import asch.io.base.view.Throwable;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.contract.DappsContract;
import asch.io.wallet.model.entity.DApp;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.dto.query.DappQueryParameters;

/**
 * Created by kimziv on 2018/1/10.
 */

public class DappsPresenter implements DappsContract.Presenter {

    private Context context;
    private DappsContract.View view;
    private IPage pager;
    private CompositeSubscription subscriptions;

    public DappsPresenter(Context ctx, DappsContract.View view) {
        this.context=ctx;
        this.view=view;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);
        initPager();
    }

    private void initPager(){
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                loadDapps(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(AppConstants.DEFAULT_PAGE_SIZE);
    }
    /*
    {
	"success": true,
	"dapps": [{
		"name": "asch-dapp-cctime",
		"description": "Decentralized news channel",
		"tags": "asch,dapp,demo,cctime",
		"link": "https://github.com/AschPlatform/asch-dapp-cctime/archive/master.zip",
		"type": 0,
		"category": 1,
		"icon": "http://o7dyh3w0x.bkt.clouddn.com/hello.png",
		"delegates": ["8b1c24a0b9ba9b9ccf5e35d0c848d582a2a22cca54d42de8ac7b2412e7dc63d4", "aa7dcc3afd151a549e826753b0547c90e61b022adb26938177904a73fc4fee36", "e29c75979ac834b871ce58dc52a6f604f8f565dea2b8925705883b8c001fe8ce", "55ad778a8ff0ce4c25cb7a45735c9e55cf1daca110cfddee30e789cb07c8c9f3", "982076258caab20f06feddc94b95ace89a2862f36fea73fa007916ab97e5946a"],
		"unlockDelegates": 3,
		"transactionId": "d352263c517195a8b612260971c7af869edca305bb64b471686323817e57b2c1"
	}],
	"count": {
		"count": 1
	}
}
     */
    private void loadDapps(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        //String address = getAccount().getAddress();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<DApp>>) subscriber -> {
            DappQueryParameters params = new DappQueryParameters()
                    //.orderByDescending("t_timestamp")
                    .setOffset(offset)
                    .setLimit(limit);
            AschResult result = AschSDK.Dapp.queryDapps(params);
            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray peersJsonArray = resultJSONObj.getJSONArray("dapps");
                List<DApp> DApps = JSON.parseArray(peersJsonArray.toJSONString(), DApp.class);
                ArrayList<DApp> filteredDApps =new ArrayList<DApp>();
                filteredDApps.add(DApps.get(0));
                subscriber.onNext(filteredDApps);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DApp>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<DApp> dapps) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageDapps(dapps);
                        } else {
                            view.displayMorePageDapps(dapps);
                        }
                        pager.finishLoad(true);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        subscriptions.clear();
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadFirstPageDapps() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageDapps() {
        pager.loadPage(false);
    }
}
