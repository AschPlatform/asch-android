package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.RecordMulitChainContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.Deposit;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.Withdraw;
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

public class RecordMulitChainPresenter implements RecordMulitChainContract.Presenter {

    private RecordMulitChainContract.View view;
    private Context context;
    //不同的币种，例如XAS、BCH
    private int assetType;
    public enum RecordType {transfer,deposit,withdraw}
    //不同的记录，例如转账、提现
    RecordType recordType;
    private String currency;
    private CompositeSubscription subscriptions;
    IPage pager;

    public RecordMulitChainPresenter(Context ctx, RecordMulitChainContract.View view,int assettype,String currency,RecordType recordType) {
        this.view = view;
        this.context = ctx;
        this.assetType = assettype;
        this.currency = currency;
        this.recordType = recordType;
        this.subscriptions=new CompositeSubscription();
        initPager();
        view.setPresenter(this);
    }


    private void initPager() {
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                if (recordType == RecordType.transfer)
                    loadTransactions(param1, param2);
                else if(recordType == RecordType.deposit)
                    loadDeposit(param1, param2);
                else if(recordType == RecordType.withdraw)
                    loadWithdraw(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(20);
    }

    private void loadDeposit(int pageIndex,int pageSize){
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String address = getAccount().getAddress();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Deposit>>) subscriber -> {
            AschResult result;
            DepositQueryParameters params = new DepositQueryParameters()
                    .setAddress(address)
                    .setCurrency(currency);

            result = AschSDK.Gateway.getDepositRecord(params);

            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray depositsJsonArray = resultJSONObj.getJSONArray("deposits");
                List<Deposit> deposits = JSON.parseArray(depositsJsonArray.toJSONString(), Deposit.class);
                subscriber.onNext(deposits);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Deposit>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<Deposit> deposits) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageTransactions(deposits);
                        } else {
                            view.displayMorePageTransactions(deposits);
                        }
                        pager.finishLoad(true);
                    }
                });
        subscriptions.add(subscription);
    }

    private void loadWithdraw(int pageIndex,int pageSize){
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String address = getAccount().getAddress();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Withdraw>>) subscriber -> {
            AschResult result;
            WithdrawQueryParameters params = new WithdrawQueryParameters()
                    .setAddress(address)
                    .setCurrency(currency);

            result = AschSDK.Gateway.getWithdrawRecord(params);

            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray withdrawsJsonArray = resultJSONObj.getJSONArray("withdrawals");
                List<Withdraw> withdraws = JSON.parseArray(withdrawsJsonArray.toJSONString(), Withdraw.class);
                subscriber.onNext(withdraws);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Withdraw>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<Withdraw> withdraws) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageTransactions(withdraws);
                        } else {
                            view.displayMorePageTransactions(withdraws);
                        }
                        pager.finishLoad(true);
                    }
                });
        subscriptions.add(subscription);
    }

    private void loadTransactions(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String address = getAccount().getAddress();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Transaction>>) subscriber -> {
            AschResult result;
            TransactionQueryParameters params = new TransactionQueryParameters()
                    .setOwnerId(address)
                    .setUia(0)
                    .orderByDescending("t_timestamp")
                    .setOffset(offset)
                    .setLimit(limit);

            if (assetType == BaseAsset.TYPE_XAS) {
                params.setCurrency(AschConst.CORE_COIN_NAME);
            }else {
                params.setCurrency(currency);
            }

            result = AschSDK.Transaction.queryTransactionsV2(params);

            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray transactionsJsonArray = resultJSONObj.getJSONArray("transfers");
                List<Transaction> transactions = JSON.parseArray(transactionsJsonArray.toJSONString(), Transaction.class);
                ArrayList<Transaction> filteredTransactions = new ArrayList<Transaction>();
                for (Transaction transaction : transactions) {
                    transaction.setType(transaction.getTransaction().getType());
                    if (transaction.getType() == TransactionType.basic_transfer.getCode()) {
                        transaction.setAssetInfo(new Transaction.AssetInfo());
                    } else if (transaction.getType() == TransactionType.UIATransferV2.getCode()) {
                        Transaction.AssetInfo asset = JSON.parseObject(transaction.getAsset(), Transaction.AssetInfo.class);
                        transaction.setAssetInfo(asset);
                    }
                    filteredTransactions.add(transaction);
                }
                subscriber.onNext(filteredTransactions);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Transaction>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<Transaction> transactions) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageTransactions(transactions);
                        } else {
                            view.displayMorePageTransactions(transactions);
                        }
                        pager.finishLoad(true);
                    }
                });
        subscriptions.add(subscription);
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

    private String getAddress() {
        return getAccount().getAddress();
    }

    @Override
    public void loadFirstPageTransactions() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageTransactions() {
        pager.loadPage(false);
    }



}
