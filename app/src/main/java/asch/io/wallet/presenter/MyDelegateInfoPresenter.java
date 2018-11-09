package asch.io.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import asch.io.base.adapter.page.IPage;
import asch.io.base.adapter.page.Page1;
import asch.io.base.view.Throwable;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.MyDelegateInfoContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.Block;
import asch.io.wallet.model.entity.Delegate;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.dto.query.BlockQueryParameters;

/**
 * Created by kimziv on 2017/12/4.
 */

public class MyDelegateInfoPresenter implements MyDelegateInfoContract.Presenter {

    private Context context;
    private MyDelegateInfoContract.View view;
    private IPage pager;
    private CompositeSubscription subscriptions;

    public MyDelegateInfoPresenter(Context context, MyDelegateInfoContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        subscriptions=new CompositeSubscription();
        initPager();
    }

    private void initPager() {
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                loadBlocks(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(AppConstants.DEFAULT_PAGE_SIZE);
    }

    private void loadBlocks(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String publicKey = getAccount().getPublicKey();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Block>>) subscriber -> {
            BlockQueryParameters params = new BlockQueryParameters()
                    .setGeneratorPublicKey(publicKey)
                    .orderByDescending("height")
                    .setOffset(offset)
                    .setLimit(limit);

            AschResult result = AschSDK.Block.queryBlocksV2(params);
            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray blocksJsonArray = resultJSONObj.getJSONArray("blocks");
                List<Block> blocks = JSON.parseArray(blocksJsonArray.toJSONString(), Block.class);
                subscriber.onNext(blocks);
                subscriber.onCompleted();
            } else {
                subscriber.onError(new Throwable(result.getError()));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Block>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<Block> blocks) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageBlocks(blocks);
                        } else {
                            view.displayMorePageBlocks(blocks);
                        }
                        pager.finishLoad(true);
                    }
                });
        subscriptions.add(subscription);
    }


    private Observable createFetchForgingStatusObservable(String publicKey){

       return Observable.create(new Observable.OnSubscribe<Boolean>(){
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                AschResult result = AschSDK.Delegate.getForgingStatus(publicKey);
                if (result.isSuccessful()) {
                    JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                    boolean enabled = resultJSONObj.getBoolean("enabled");
                    subscriber.onNext(enabled);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(result.getError()));
                }
            }
        });
    }

    private Observable createFetchDelegateInfoObservable(String publicKey){

        return Observable.create(new Observable.OnSubscribe<Delegate>(){
            @Override
            public void call(Subscriber<? super Delegate> subscriber) {
                AschResult result = AschSDK.Delegate.getDelegateByPublicKey(publicKey);
                if (result.isSuccessful()) {
                    JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                    JSONObject delegateJsonObj = resultJSONObj.getJSONObject("delegate");
                    Delegate delegate = JSON.parseObject(delegateJsonObj.toJSONString(), Delegate.class);
                    subscriber.onNext(delegate);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(result.getError()));
                }
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadDelegateInfo() {

    }

    @Override
    public void registerDelegate(String delegateName) {

    }

    @Override
    public void loadFirstPageBlocks() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageBlocks() {
        pager.loadPage(false);
    }

    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
