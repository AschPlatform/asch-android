package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.UIException;
import asch.so.wallet.AppConstants;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.TransferAsset;
import asch.so.wallet.model.entity.UIATransferAsset;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.TransactionType;
import so.asch.sdk.dto.query.QueryParameters;
import so.asch.sdk.dto.query.TransactionQueryParameters;

/**
 * Created by kimziv on 2017/10/13.
 */

public class AssetTransactionsPresenter implements AssetTransactionsContract.Presenter {

    private AssetTransactionsContract.View view;
    private Context context;
    private String currency;
    IPage pager;

    public AssetTransactionsPresenter(Context ctx, AssetTransactionsContract.View view, String currency) {
        this.view = view;
        this.context = ctx;
        this.currency = currency;
        initPager();
        view.setPresenter(this);
    }


    private void initPager() {
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                loadTransactions(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(20);
    }

    private boolean isUIA() {
        return !AppConstants.XAS_NAME.equals(currency);
    }

    private void loadTransactions(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String address = getAccount().getAddress();
        Observable.create((Observable.OnSubscribe<List<Transaction>>) subscriber -> {
            AschResult result;
            if (isUIA()) {
                result = AschSDK.UIA.getTransactions(address, currency, limit, offset);
            } else {
                TransactionQueryParameters params = new TransactionQueryParameters()
                        .setSenderId(address)
                        .setRecipientId(address)
                        .setUia(0)
                        .orderByDescending("t_timestamp")
                        .setOffset(offset)
                        .setLimit(limit);
                result = AschSDK.Transaction.queryTransactions(params);
            }

            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray transactionsJsonArray = resultJSONObj.getJSONArray("transactions");
                List<Transaction> transactions = JSON.parseArray(transactionsJsonArray.toJSONString(), Transaction.class);
                ArrayList<Transaction> filteredTransactions = new ArrayList<Transaction>();
                for (Transaction transaction : transactions) {
                    if (!isUIA() && transaction.getType() != TransactionType.Transfer.getCode()) {
                        continue;
                    }
                    if (transaction.getType() == TransactionType.Transfer.getCode()) {
                        transaction.setAssetInfo(new TransferAsset());
                    } else if (transaction.getType() == TransactionType.UIATransfer.getCode()) {
                        UIATransferAsset asset = JSON.parseObject(transaction.getAsset(), UIATransferAsset.class);
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
                    public void onError(Throwable e) {
                        view.displayError(new UIException("网络错误"));
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
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

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

//    @Override
//    public void loadTransactions(String currency, boolean isUIA) {
//        String address = getAddress();
//        Observable.create(new Observable.OnSubscribe<List<Transaction>>() {
//
//            @Override
//            public void call(Subscriber<? super List<Transaction>> subscriber) {
//
//                AschResult result;
//                if (isUIA) {
//                    result = AschSDK.UIA.getTransactions(address, currency, 20, 0);
//                } else {
//                    TransactionQueryParameters params = new TransactionQueryParameters()
//                            .setSenderId(address)
//                            .setRecipientId(address)
//                            .setUia(isUIA ? 1 : 0)
//                            .orderByDescending("t_timestamp");
//                    result = AschSDK.Transaction.queryTransactions(params);
//                }
//
//                if (result.isSuccessful()) {
//                    JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
//                    JSONArray transactionsJsonArray = resultJSONObj.getJSONArray("transactions");
//                    List<Transaction> transactions = JSON.parseArray(transactionsJsonArray.toJSONString(), Transaction.class);
//                    ArrayList<Transaction> filteredTransactions = new ArrayList<Transaction>();
//                    for (Transaction transaction : transactions) {
//                        if (!isUIA && transaction.getType() != TransactionType.Transfer.getCode()) {
//                            continue;
//                        }
//                        if (transaction.getType() == TransactionType.Transfer.getCode()) {
//                            transaction.setAssetInfo(new TransferAsset());
//                        } else if (transaction.getType() == TransactionType.UIATransfer.getCode()) {
//                            UIATransferAsset asset = JSON.parseObject(transaction.getAsset(), UIATransferAsset.class);
//                            transaction.setAssetInfo(asset);
//
//                        }
//                        filteredTransactions.add(transaction);
//                    }
//
//                    subscriber.onNext(filteredTransactions);
//                    subscriber.onCompleted();
//                } else {
//                    subscriber.onError(result.getException());
//                }
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<List<Transaction>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        view.displayError(new UIException("网络错误"));
//                    }
//
//                    @Override
//                    public void onNext(List<Transaction> transactions) {
//                        view.displayTransactions(transactions);
//                    }
//                });
////                .subscribe(new Action1<List<Transaction>>() {
////                    @Override
////                    public void call(List<Transaction> transactions) {
////                        view.displayTransactions(transactions);
////                    }
////                });
//    }
//
//    @Override
//    public void loadMoreTransactions(String currency, boolean isUIA) {
//
//    }


}
