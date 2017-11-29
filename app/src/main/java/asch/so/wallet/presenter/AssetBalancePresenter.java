package asch.so.wallet.presenter;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import asch.so.base.view.UIException;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetBalanceContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.FullAccount;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/20.
 */

public class AssetBalancePresenter implements AssetBalanceContract.Presenter,Observer {
    private static  final  String TAG=AssetBalancePresenter.class.getSimpleName();

    private final  AssetBalanceContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;

    public AssetBalancePresenter(AssetBalanceContract.View view) {
        this.view = view;
        view.setPresenter(this);
        this.subscriptions=new CompositeSubscription();
        AccountsManager.getInstance().addObserver(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        this.subscriptions.clear();
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void loadAccount() {
        Account account =getAccount();
        if (account!=null) {
            view.displayAccount(account);
        }
    }

    @Override
    public void loadAssets(){

       Observable<FullAccount> observable = AccountsManager.getInstance().createLoadFullAccountObservable();
      Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FullAccount>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("xasObservable error:",e.toString());
                        view.displayError(new UIException("获取余额错误"));
                    }

                    @Override
                    public void onNext(FullAccount fullAccount) {
                        Log.d(TAG,"FullAccount info:"+fullAccount.getAccount().getAddress()+" balances:"+fullAccount.getBalances().toString());
                        getAccount().setFullAccount(fullAccount);

                        if (fullAccount!=null){
                            view.displayXASBalance(fullAccount.getBalances().get(0));
                        }
                        view.displayAssets(fullAccount.getBalances());
                    }
                });
      subscriptions.add(subscription);
    }


    @Override
    public void update(java.util.Observable observable, Object o) {
        if (observable instanceof AccountsManager){
            loadAccount();
            loadAssets();
        }
    }
}
