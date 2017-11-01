package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    private String getAddress(){
        return getAccount().getAddress();
    }

    @Override
    public void loadTransactions(String currency, boolean isUIA) {
        String address=getAddress();
     Observable.create(new Observable.OnSubscribe<List<Transaction>>() {

            @Override
            public void call(Subscriber<? super List<Transaction>> subscriber) {

                AschResult result=null;
                if (isUIA){
                    result = AschSDK.UIA.getTransactions(address,currency,20,0);
                }else {
                    TransactionQueryParameters params=new TransactionQueryParameters()
                            .setSenderId(address)
                            .setRecipientId(address)
                            .setUia(isUIA?1:0)
                            .orderByDescending("t_timestamp")
                            ;
                    //.setCurrency("XAS");
                    result = AschSDK.Transaction.queryTransactions(params);
                }

                if (result.isSuccessful()){
                    JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                    JSONArray transactionsJsonArray=resultJSONObj.getJSONArray("transactions");
                    List<Transaction> transactions= JSON.parseArray(transactionsJsonArray.toJSONString(),Transaction.class);
                    ArrayList<Transaction> filteredTransactions=new ArrayList<Transaction>();
                    for (Transaction transaction:transactions){
                        if (!isUIA  && transaction.getType()!=TransactionType.Transfer.getCode()){
                            continue;
                        }
                        if (transaction.getType()==TransactionType.Transfer.getCode()){
                            transaction.setAssetInfo(new TransferAsset());
                        }else if (transaction.getType()==TransactionType.UIATransfer.getCode()){
                            UIATransferAsset  asset= JSON.parseObject(transaction.getAsset(), UIATransferAsset.class);
                            transaction.setAssetInfo(asset);

                        }
                        filteredTransactions.add(transaction);
                    }

                    subscriber.onNext(filteredTransactions);
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

    @Override
    public void loadMoreTransactions(String currency, boolean isUIA) {

    }


}
