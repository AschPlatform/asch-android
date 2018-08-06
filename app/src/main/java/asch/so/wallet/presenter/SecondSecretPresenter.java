package asch.so.wallet.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.SecondSecretContract;
import asch.so.wallet.model.entity.Account;
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
    public void storeSecondPassword(String accountPasswd, String secondSecret) {

        String encryptSecret = getAccount().getEncryptSeed();
        Subscription subscription = Observable.create((Observable.OnSubscribe<AschResult>) subscriber -> {
            String decryptSecret = Account.decryptSecret(accountPasswd, encryptSecret);
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

    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
