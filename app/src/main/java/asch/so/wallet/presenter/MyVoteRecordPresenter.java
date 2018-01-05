package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.MyVoteRecordContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.MyVoteRecordAdapter;
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

public class MyVoteRecordPresenter implements MyVoteRecordContract.Presenter {
    private static final String TAG= MyVoteRecordPresenter.class.getSimpleName();
    private Context context;
    private MyVoteRecordContract.View view;
    private CompositeSubscription subscriptions;
    IPage pager;

    public MyVoteRecordPresenter(Context context, MyVoteRecordContract.View view) {
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
            AschResult result = AschSDK.Account.getVotedDelegates(address);
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
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
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


    @Override
    public void downVoteForDelegates(List<Delegate> delegates, String secret, String secondSecret) {
        String[] pubKeys =new String[delegates.size()];
        int i =0;
        for (Delegate delegate : delegates) {
            pubKeys[i++]=delegate.getPublicKey();
        }
        if (pubKeys.length==0)
            return;

        Subscription subscription = Observable.create((Observable.OnSubscribe<AschResult>) subscriber -> {
            AschResult result = AschSDK.Account.vote(null,pubKeys,secret,secondSecret);
            if (result.isSuccessful()) {
                subscriber.onNext(result);
                subscriber.onCompleted();
            } else {
                subscriber.onError(new Throwable(result.getError()));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        LogUtils.dTag(TAG,"vote result:"+e==null?"vote result error":e.toString());
                        view.displayDownVoteResult(false, AppUtil.extractInfoFromError(context, e));
                    }

                    @Override
                    public void onNext(AschResult result) {
                        LogUtils.dTag(TAG,"vote result:"+result.getRawJson());
                        view.displayDownVoteResult(true, context.getString(R.string.cancel_vote_success));
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
