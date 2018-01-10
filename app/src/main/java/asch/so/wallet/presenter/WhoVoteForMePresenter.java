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
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.WhoVoteForMeContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Voter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/11/29.
 */

public class WhoVoteForMePresenter implements WhoVoteForMeContract.Presenter {
    private Context context;
    private WhoVoteForMeContract.View view;
    private CompositeSubscription subscriptions;
    IPage pager;

    public WhoVoteForMePresenter(Context context, WhoVoteForMeContract.View view) {
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
                loadVoters(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(AppConstants.DEFAULT_PAGE_SIZE);
    }

    private void loadVoters(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String publicKey = getAccount().getPublicKey();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Voter>>) subscriber -> {
//            DelegateQueryParameters params =new DelegateQueryParameters()
//                    .setAddress(address)
//                    .orderByAscending("rate")
//                    .setOffset(offset)
//                    .setLimit(limit);

            AschResult result = AschSDK.Delegate.getVoters(publicKey);
            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray votersJsonArray = resultJSONObj.getJSONArray("accounts");
                List<Voter> voters = JSON.parseArray(votersJsonArray.toJSONString(), Voter.class);
                subscriber.onNext(voters);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Voter>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<Voter> voters) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageVoters(voters);
                        } else {
                            view.displayMorePageVoters(voters);
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
    public void loadFirstPageVoters() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageVoters() {
        pager.loadPage(false);
    }
}
