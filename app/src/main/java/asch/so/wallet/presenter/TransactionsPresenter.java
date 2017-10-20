package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import asch.so.wallet.TestData;
import asch.so.wallet.contract.TransactionsContract;
import asch.so.wallet.model.entity.Transaction;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
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

        Observable.create(new Observable.OnSubscribe<List<Transaction>>() {

            @Override
            public void call(Subscriber<? super List<Transaction>> subscriber) {
                TransactionQueryParameters params=new TransactionQueryParameters()
                        .setSenderId(TestData.address)
                        .setRecipientId(TestData.address)
                        .setOffset(0)
                        .setLimit(10);
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
                .subscribe(new Action1<List<Transaction>>() {
                    @Override
                    public void call(List<Transaction> transactions) {
                        view.displayTranscations(transactions);
                    }
                });
    }

    @Override
    public void refreshTransactions() {
        Observable.create(new Observable.OnSubscribe<List<Transaction>>() {

            @Override
            public void call(Subscriber<? super List<Transaction>> subscriber) {
                TransactionQueryParameters params=new TransactionQueryParameters()
                        .setSenderId(TestData.address)
                        .setRecipientId(TestData.address)
                        .setOffset(0)
                        .setLimit(10);
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
                .subscribe(new Action1<List<Transaction>>() {
                    @Override
                    public void call(List<Transaction> transactions) {
                        view.displayMoreTranscations(transactions);
                    }
                });
    }

    @Override
    public void loadMoreTransactions() {
        Observable.create(new Observable.OnSubscribe<List<Transaction>>() {

            @Override
            public void call(Subscriber<? super List<Transaction>> subscriber) {
                TransactionQueryParameters params=new TransactionQueryParameters()
                        .setSenderId(TestData.address)
                        .setRecipientId(TestData.address)
                        .setOffset(0)
                        .setLimit(10);
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
                .subscribe(new Action1<List<Transaction>>() {
                    @Override
                    public void call(List<Transaction> transactions) {
                        view.displayMoreTranscations(transactions);
                    }
                });
    }
}
