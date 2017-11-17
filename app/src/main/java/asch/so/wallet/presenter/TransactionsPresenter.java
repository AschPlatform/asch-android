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

    IPage pager;

    public TransactionsPresenter(Context context, TransactionsContract.View view) {
        this.context = context;
        this.view = view;
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

    private void loadTransactions(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String address = getAccount().getAddress();
        Observable.create((Observable.OnSubscribe<List<Transaction>>) subscriber -> {
            TransactionQueryParameters params = new TransactionQueryParameters()
                    .setSenderId(address)
                    .setRecipientId(address)
                    .setOffset(offset)
                    .setLimit(limit);
            AschResult result = AschSDK.Transaction.queryTransactions(params);
            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray transactionsJsonArray = resultJSONObj.getJSONArray("transactions");
                List<Transaction> transactions = JSON.parseArray(transactionsJsonArray.toJSONString(), Transaction.class);
                ArrayList<Transaction> filteredTransactions = new ArrayList<Transaction>();
                for (Transaction transaction : transactions) {
//                    if (!isUIA() && transaction.getType() != TransactionType.Transfer.getCode()) {
//                        continue;
//                    }
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
    public void loadFirstPageTransactions() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageTransactions() {
        pager.loadPage(false);
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
}
