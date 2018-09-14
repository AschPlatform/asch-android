package asch.so.wallet.presenter;

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.AccountsActivity;
import asch.so.wallet.contract.SecondSecretContract;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.FullAccount;
import asch.so.wallet.util.AppUtil;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.FeeCalculater;
import so.asch.sdk.impl.Validation;

/**
 * Created by haizeiwang on 2018/1/21.
 */

public class SecondSecretPresenter implements SecondSecretContract.Presenter{
    private static  final  String TAG=SecondSecretPresenter.class.getSimpleName();
    private SecondSecretContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;

    public SecondSecretPresenter(Context context, SecondSecretContract.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
        this.subscriptions=new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void storeSecondPassword( String secondSecret,String password) {

        String decryptSecret = AccountSecurity.decryptSecret(password);
        Subscription subscription = Observable.create((Observable.OnSubscribe<AschResult>) subscriber -> {
            if (!Validation.isValidSecret(decryptSecret)) {
                subscriber.onError(new Throwable(context.getString(R.string.account_password_error)));
            } else {
                AschResult result = AschSDK.Signature.setSignature(decryptSecret,secondSecret,null,null);
                if (result.isSuccessful()) {
                    getAccount().getFullAccount().getAccount().setSecondSignature(true);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(result.getError()));
                }
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(e);
                    }

                    @Override
                    public void onNext(AschResult result) {

                        view.displaySetSecondSecretResult(true, context.getString(R.string.set_second_secret_success));
                    }
                });
        subscriptions.add(subscription);
    }


    private void refreshAccountState(){
        Observable<FullAccount> observable = AccountsManager.getInstance().createLoadFullAccountObservable();
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
//                        view.displayError(new Throwable(context.getString(R.string.balance_get_error)));
                    }

                    @Override
                    public void onNext(FullAccount fullAccount) {
                        LogUtils.dTag(TAG,"FullAccount info:"+fullAccount.getAccount().getAddress()+" balances:"+fullAccount.getBalances().toString());
                        getAccount().setFullAccount(fullAccount);


                    }
                });
        subscriptions.add(subscription);
    }

    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
