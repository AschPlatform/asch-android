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
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.RecordMulitChainContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.BaseAsset;
import asch.io.wallet.model.entity.Deposit;
import asch.io.wallet.model.entity.Transaction;
import asch.io.wallet.model.entity.Withdraw;
import asch.io.wallet.model.entity.WithdrawAsset;
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
                    loadDeposit();
                else if(recordType == RecordType.withdraw)
                    loadWithdraw();
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(20);
    }


    private void loadDeposit(){
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
                        displayError();
                    }

                    @Override
                    public void onNext(List<Deposit> deposits) {
                        displayNext(deposits);
                    }
                });
        subscriptions.add(subscription);
    }

    private void loadWithdraw(){
        String address = getAccount().getAddress();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Withdraw>>) subscriber -> {
            AschResult result;
            WithdrawQueryParameters params = new WithdrawQueryParameters()
                    .setAddress(address)
                    .setCurrency(currency);

            result = AschSDK.Gateway.getWithdrawRecord(params);

            if (result.isSuccessful()) {
                //这里面的内部类不能自动解析。只能拆开来手解。
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray withdrawsJsonArray = resultJSONObj.getJSONArray("withdrawals");
                List<Withdraw> withdraws = new ArrayList<>();
                for (int i=0;i<withdrawsJsonArray.size();i++){
                    JSONObject object = withdrawsJsonArray.getJSONObject(i);
                    int time = object.getIntValue("timestamp");
                    String amount = object.getString("amount");
                    Withdraw withdraw =new Withdraw();
                    withdraw.setAmount(Long.valueOf(amount));
                    withdraw.setTimestamp(time);
                    withdraw.setRecipientId(object.getString("recipientId"));
                    JSONObject asset = object.getJSONObject("asset");
                    String symbol = asset.getString("symbol");
                    WithdrawAsset wa =  new WithdrawAsset();
                    wa.setSymbol(symbol);
                    wa.setPrecision(asset.getInteger("precision"));
                    withdraw.setAsset(wa);
                    withdraws.add(withdraw);

                }
//                List<Withdraw> withdraws = JSON.parseArray(withdrawsJsonArray.toJSONString(), Withdraw.class);
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
                        displayError();
                    }

                    @Override
                    public void onNext(List<Withdraw> withdraws) {
                       displayNext(withdraws);
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
                       displayError();
                    }

                    @Override
                    public void onNext(List<Transaction> transactions) {
                        displayNext(transactions);

                    }
                });
        subscriptions.add(subscription);
    }

    private void displayError(){
        view.displayError(new Throwable(context.getString(R.string.net_error)));
        pager.finishLoad(true);
    }

    private void displayNext(List<?> records){
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
