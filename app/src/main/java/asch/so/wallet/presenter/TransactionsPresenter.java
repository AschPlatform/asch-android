package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.view.UIException;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.TransactionsContract;
import asch.so.wallet.model.entity.Account;
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
import so.asch.sdk.dto.query.TransactionQueryParameters;

/**
 * Created by kimziv on 2017/10/18.
 */

public class TransactionsPresenter implements TransactionsContract.Presenter {

    private Context context;
    private TransactionsContract.View view;
    public TransactionsPresenter(Context context, TransactionsContract.View view) {
        this.context=context;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void loadTransactions() {
        String address=getAccount().getAddress();
        Observable.create(new Observable.OnSubscribe<List<Transaction>>() {

            @Override
            public void call(Subscriber<? super List<Transaction>> subscriber) {
                TransactionQueryParameters params=new TransactionQueryParameters()
                        .setSenderId(address)
                        .setRecipientId(address)
                        .setOffset(0)
                        .setLimit(20);
                AschResult result = AschSDK.Transaction.queryTransactions(params);
                if (result.isSuccessful()){
                    JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                    JSONArray transactionsJsonArray=resultJSONObj.getJSONArray("transactions");
                    List<Transaction> balances= JSON.parseArray(transactionsJsonArray.toJSONString(),Transaction.class);
                    // list.addAll(balances);
                    subscriber.onNext(balances);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(result.getException());
                }
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
                    }

                    @Override
                    public void onNext(List<Transaction> transactions) {
                        view.displayTranscations(transactions);
                    }
                });
//                .subscribe(new Action1<List<Transaction>>() {
//                    @Override
//                    public void call(List<Transaction> transactions) {
//                        view.displayTranscations(transactions);
//                    }
//                });
    }

    @Override
    public void refreshTransactions() {
        String address=getAccount().getAddress();
        Observable.create(new Observable.OnSubscribe<List<Transaction>>() {

            @Override
            public void call(Subscriber<? super List<Transaction>> subscriber) {
                TransactionQueryParameters params=new TransactionQueryParameters()
                        .setSenderId(address)
                        .setRecipientId(address)
                        .orderByDescending("t_timestamp")
                        .setOffset(0)
                        .setLimit(20);
                AschResult result = AschSDK.Transaction.queryTransactions(params);
                if (result.isSuccessful()){
                    JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                    JSONArray transactionsJsonArray=resultJSONObj.getJSONArray("transactions");
                    List<Transaction> balances= JSON.parseArray(transactionsJsonArray.toJSONString(),Transaction.class);
                    // list.addAll(balances);
                    subscriber.onNext(balances);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(result.getException());
                }
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
                    }

                    @Override
                    public void onNext(List<Transaction> transactions) {
                        view.displayMoreTranscations(transactions);
                    }
                });
//                .subscribe(new Action1<List<Transaction>>() {
//                    @Override
//                    public void call(List<Transaction> transactions) {
//                        view.displayMoreTranscations(transactions);
//                    }
//                });
    }

    @Override
    public void loadMoreTransactions() {

        String address=getAccount().getAddress();

        Observable.create(new Observable.OnSubscribe<List<Transaction>>() {

            @Override
            public void call(Subscriber<? super List<Transaction>> subscriber) {
                TransactionQueryParameters params=new TransactionQueryParameters()
                        .setSenderId(address)
                        .setRecipientId(address)
                        .setOffset(0)
                        .setLimit(10);
                AschResult result = AschSDK.Transaction.queryTransactions(params);
                if (result.isSuccessful()){
                    JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                    JSONArray transactionsJsonArray=resultJSONObj.getJSONArray("transactions");
                    List<Transaction> transactions= JSON.parseArray(transactionsJsonArray.toJSONString(),Transaction.class);
//                    ArrayList<Transaction> filteredTransactions=new ArrayList<Transaction>();
//                    for (Transaction transaction:transactions){
//                        if (transaction.getType() == TransactionType.Transfer.getCode()){
//                            continue;
//                        }
//                        if (transaction.getType()==TransactionType.Transfer.getCode()){
//                            transaction.setAssetInfo(new TransferAsset());
//                        }else if (transaction.getType()==TransactionType.UIATransfer.getCode()){
//                            UIATransferAsset asset= JSON.parseObject(transaction.getAsset(), UIATransferAsset.class);
//                            transaction.setAssetInfo(asset);
//
//                        }
//                        filteredTransactions.add(transaction);
//                    }
                    subscriber.onNext(transactions);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(result.getException());
                }
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

                    }

                    @Override
                    public void onNext(List<Transaction> transactions) {
                        view.displayMoreTranscations(transactions);
                    }
                });
    }

    //private List<Transaction> filterTransactions

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
