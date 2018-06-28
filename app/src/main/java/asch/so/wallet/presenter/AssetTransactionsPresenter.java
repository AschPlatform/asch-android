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
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.TransferAsset;
import asch.so.wallet.model.entity.UIATransferAsset;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.TransactionType;
import so.asch.sdk.dto.query.TransactionQueryParameters;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/10/13.
 */

public class AssetTransactionsPresenter implements AssetTransactionsContract.Presenter {

    private AssetTransactionsContract.View view;
    private Context context;
    private String currency;
    private CompositeSubscription subscriptions;
    IPage pager;

    public AssetTransactionsPresenter(Context ctx, AssetTransactionsContract.View view, String currency) {
        this.view = view;
        this.context = ctx;
        this.currency = currency;
        this.subscriptions=new CompositeSubscription();
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
        //String pubKey = getAccount().getPublicKey();
      Subscription subscription = Observable.create((Observable.OnSubscribe<List<Transaction>>) subscriber -> {
            AschResult result;
          TransactionQueryParameters params = new TransactionQueryParameters()
//                        .setSenderPublicKey(pubKey)
//                        .setOwnerPublicKey(pubKey)
                  //.setCurrency(AschConst.CORE_COIN_NAME)
                  .setOwnerId(address)
//                        .setSenderId(address)
//                        .setRecipientId(address)
                  .setUia(0)
                  .orderByDescending("t_timestamp")
                  .setOffset(offset)
                  .setLimit(limit);
            if (isUIA()) {
                params.setCurrency(currency);
                //result = AschSDK.UIA.getTransactions(address, currency, limit, offset);
            } else {
                params.setCurrency(AschConst.CORE_COIN_NAME);
//                TransactionQueryParameters params = new TransactionQueryParameters()
////                        .setSenderPublicKey(pubKey)
////                        .setOwnerPublicKey(pubKey)
//                        .setCurrency(AschConst.CORE_COIN_NAME)
//                        .setOwnerId(address)
////                        .setSenderId(address)
////                        .setRecipientId(address)
//                        .setUia(0)
//                        .orderByDescending("t_timestamp")
//                        .setOffset(offset)
//                        .setLimit(limit);

            }

          result = AschSDK.Transaction.queryTransactionsV2(params);

            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray transactionsJsonArray = resultJSONObj.getJSONArray("transfers");
                List<Transaction> transactions = JSON.parseArray(transactionsJsonArray.toJSONString(), Transaction.class);
                ArrayList<Transaction> filteredTransactions = new ArrayList<Transaction>();
                for (Transaction transaction : transactions) {
                    transaction.setType(transaction.getTransaction().getType());
//                    if (!isUIA() && transaction.getType() != TransactionType.Transfer.getCode()) {
//                        continue;
//                    }
                    if (transaction.getType() == TransactionType.TransferV2.getCode()) {
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
    public void subscribe() {

    }

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
