package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import asch.so.wallet.TestData;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Transaction;
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
 * Created by kimziv on 2017/10/13.
 */

public class AssetTransactionsPresenter implements AssetTransactionsContract.Presenter {

    private AssetTransactionsContract.View view;
    private Context context;

    public AssetTransactionsPresenter(Context context, AssetTransactionsContract.View view) {
        this.view = view;
        this.context = context;
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
                .setOwnerAddress(TestData.address)
                .setTransactionType(TransactionType.Transfer)
                .setUia(0)
                .setCurrency("XAS");        ;
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
                        view.displayTransactions(transactions);
                    }
                });
    }
}
