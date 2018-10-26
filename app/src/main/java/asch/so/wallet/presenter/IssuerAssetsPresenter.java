package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.Throwable;
import asch.so.wallet.P;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.IssuerAssetsContract;
import asch.so.wallet.contract.RecordMulitChainContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.Deposit;
import asch.so.wallet.model.entity.IssuerAssets;
import asch.so.wallet.model.entity.IssuerInfo;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.Withdraw;
import asch.so.wallet.model.entity.WithdrawAsset;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.TransactionType;
import so.asch.sdk.dto.query.DepositQueryParameters;
import so.asch.sdk.dto.query.TransactionQueryParameters;
import so.asch.sdk.dto.query.WithdrawQueryParameters;
import so.asch.sdk.impl.AschConst;

/**
 * Created by Deng on 2018年09月25日
 */

public class IssuerAssetsPresenter implements IssuerAssetsContract.Presenter {
    private static final String TAG = IssuerAssetsPresenter.class.getSimpleName();
    private IssuerAssetsContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;
    IPage pager;

    public IssuerAssetsPresenter(Context ctx, IssuerAssetsContract.View view) {
        this.view = view;
        this.context = ctx;
        this.subscriptions=new CompositeSubscription();
        initPager();
        view.setPresenter(this);
    }

    private void initPager() {
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                loadAssets(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(20);
    }

    private void loadAssets(int pageIndex, int pageSize){
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        ArrayList<IssuerAssets> list=new ArrayList<IssuerAssets>();
        String address = getAccount().getAddress();
        Observable  uiaObservable =
                Observable.create(new Observable.OnSubscribe<List<IssuerAssets>>(){
                    @Override
                    public void call(Subscriber<? super List<IssuerAssets>> subscriber) {
                        AschResult result = AschSDK.UIA.queryIssuerAssets(address,limit,offset);
                        LogUtils.iTag(TAG,result.getRawJson());

                        if (result.isSuccessful()){
                            JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                            JSONArray balanceJsonArray=resultJSONObj.getJSONArray("assets");
                            List<IssuerAssets> assets= JSON.parseArray(balanceJsonArray.toJSONString(),IssuerAssets.class);
                            list.addAll(assets);
                            subscriber.onNext(list);
                            subscriber.onCompleted();
                        }else{
                            subscriber.onError(result.getException());
                        }
                    }
                });

        Subscription subscription = uiaObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<IssuerAssets>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable throwable) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List result) {

                        if (pager.isFirstPage()) {
                            view.displayFirstPageRecords(result);
                        } else {
                            view.displayMorePageRecords(result);
                        }
                        pager.finishLoad(true);

                    }
                });
        subscriptions.add(subscription);
    }

    public void loadIsIssuer(){
        ArrayList<IssuerInfo> list=new ArrayList<IssuerInfo>();
        String address = getAccount().getAddress();
        Observable  uiaObservable =
                Observable.create(new Observable.OnSubscribe<IssuerInfo>(){
                    @Override
                    public void call(Subscriber<? super IssuerInfo> subscriber) {
                        AschResult result = AschSDK.UIA.getIssuer(address);
                        LogUtils.iTag(TAG,result.getRawJson());
                        if (result.isSuccessful()){
                            JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                            IssuerInfo assets = null;
                            if (result.getRawJson().contains("issuer")) {
                                JSONObject issuer = resultJSONObj.getJSONObject("issuer");
                                assets = JSONObject.parseObject(issuer.toJSONString(), IssuerInfo.class);
                            }
                            subscriber.onNext(assets);
                            subscriber.onCompleted();
                        }else{
                            subscriber.onError(result.getException());
                        }
                    }
                });
        Subscription subscription = uiaObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<IssuerInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable throwable) {
                        view.displayError(throwable);
                    }

                    @Override
                    public void onNext(IssuerInfo assets) {

                        if (assets==null){
                            view.showRegisterIssuerDialog();
                        }else {
                            view.startRegisterAssetsActivity();
                        }

                    }
                });
        subscriptions.add(subscription);

    }

    private void displayError(){
        view.displayError(new Throwable(context.getString(R.string.net_error)));
        pager.finishLoad(true);
    }

    private void displayNext(List<IssuerAssets> records){
        if (pager.isFirstPage()) {
            view.displayFirstPageRecords(records);
        } else {
            view.displayMorePageRecords(records);
        }
        pager.finishLoad(true);
    }

    @Override
    public void subscribe() { }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void loadFirstPageRecords() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageRecords() {
        pager.loadPage(false);
    }



}
