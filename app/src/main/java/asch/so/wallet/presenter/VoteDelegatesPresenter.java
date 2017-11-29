package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.UIException;
import asch.so.wallet.AppConstants;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.VoteDelegatesContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Delegate;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.dto.query.DelegateQueryParameters;

/**
 * Created by kimziv on 2017/11/29.
 */

public class VoteDelegatesPresenter implements VoteDelegatesContract.Presenter {
    private Context context;
    private VoteDelegatesContract.View view;
    private CompositeSubscription subscriptions;
    IPage pager;

    public VoteDelegatesPresenter(Context context, VoteDelegatesContract.View view) {
        this.context = context;
        this.view = view;
        initPager();
        subscriptions=new CompositeSubscription();
        view.setPresenter(this);
    }

    private void initPager() {
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                loadDelegates(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(AppConstants.DEFAULT_PAGE_SIZE);
    }

    private void loadDelegates(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String address = getAccount().getAddress();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Delegate>>) subscriber -> {
            DelegateQueryParameters params =new DelegateQueryParameters()
                    .setAddress(address)
                    .orderByAscending("rate")
                    .setOffset(offset)
                    .setLimit(limit);

            AschResult result = AschSDK.Delegate.queryDelegates(params);
            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray delegatesJsonArray = resultJSONObj.getJSONArray("delegates");
                List<Delegate> delegates = JSON.parseArray(delegatesJsonArray.toJSONString(), Delegate.class);
                subscriber.onNext(delegates);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Delegate>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.displayError(new UIException("网络错误"));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<Delegate> delegates) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageDelegates(delegates);
                        } else {
                            view.displayMorePageDelegates(delegates);
                        }
                        pager.finishLoad(true);
                    }
                });
        subscriptions.add(subscription);
    }


    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadFirstPageDelegates() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageDelegates() {
        pager.loadPage(false);
    }
}
