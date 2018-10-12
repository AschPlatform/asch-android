package asch.so.wallet.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import java.util.Observer;

import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.AssetManager;
import asch.so.wallet.contract.AssetBalanceContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.FullAccount;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2017/9/20.
 */

public class AssetBalancePresenter implements AssetBalanceContract.Presenter,Observer {
    private static  final  String TAG=AssetBalancePresenter.class.getSimpleName();

    private final  AssetBalanceContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;

    public AssetBalancePresenter(Context ctx,AssetBalanceContract.View view) {
        this.context=ctx;
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
        AccountsManager.getInstance().getCurrentAccount();
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

       Observable<FullAccount> observable = AccountsManager.getInstance().loadAccountAndAssetsObservable();
      Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FullAccount>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        LogUtils.dTag("xasObservable error:",e.toString());
                        view.displayError(e);
                    }

                    @Override
                    public void onNext(FullAccount fullAccount) {
                        LogUtils.dTag(TAG,"FullAccount info:"+fullAccount.getAccount().getAddress()+" balances:"+fullAccount.getAccount().getBalance().toString());
                        getAccount().setFullAccount(fullAccount);
                        view.displayAssets(AssetManager.getInstance().queryAssetsForShow());
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

    @Override
    public void editAssets() {
        view.displayAssets(AssetManager.getInstance().queryAssetsForShow());
    }
}
