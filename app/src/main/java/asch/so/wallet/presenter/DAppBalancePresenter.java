package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.contract.DAppBalanceContract;
import asch.so.wallet.contract.PeesContact;
import asch.so.wallet.model.entity.DAppBalance;
import asch.so.wallet.model.entity.PeerNode;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.dto.query.PeerQueryParameters;

/**
 * Created by kimziv on 2018/2/9.
 */

public class DAppBalancePresenter implements DAppBalanceContract.Presenter {

    private Context context;
    private DAppBalanceContract.View view;
    private String dappId;
    private IPage pager;
    private CompositeSubscription subscriptions;
    public DAppBalancePresenter(Context context, DAppBalanceContract.View view, String dappID) {
        this.context = context;
        this.view = view;
        this.dappId=dappID;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);
        initPager();
    }

    private void initPager(){
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                loadBalances(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(AppConstants.DEFAULT_PAGE_SIZE);
    }

    private void loadBalances(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        //String address = getAccount().getAddress();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<DAppBalance>>) subscriber -> {
            AschResult result = AschSDK.Dapp.queryDappBalances(this.dappId);
            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray peersJsonArray = resultJSONObj.getJSONArray("balances");
                List<DAppBalance> balances = JSON.parseArray(peersJsonArray.toJSONString(), DAppBalance.class);
                subscriber.onNext(balances);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DAppBalance>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<DAppBalance> peers) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageBalances(peers);
                        } else {
                            view.displayMorePageBalances(peers);
                        }
                        pager.finishLoad(true);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadFirstPageBalances() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageBalances() {
        pager.loadPage(false);
    }

}
