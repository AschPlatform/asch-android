package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConfig;
import asch.so.wallet.ApplicationModule;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountImportContract;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.SecurityException;

/**
 * Created by kimziv on 2017/9/22.
 */

public class AccountImportPresenter implements AccountImportContract.Presenter {
    private  static  final  String TAG=AccountImportPresenter.class.getSimpleName();
    AccountImportContract.View view;
    CompositeSubscription subscriptions;


    @Inject
    AccountsDao accountsDao;

    @Inject
    public AccountImportPresenter(Context ctx, AccountImportContract.View view) {
        this.view = view;
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
    public void importAccount(String secret, String name, String password, String hint) {
        Observable<Account> observable =  Observable.create(new Observable.OnSubscribe<Account>() {

            @Override
            public void call(Subscriber<? super Account> subscriber) {
                if (AccountsManager.getInstance().hasAccountForSeed(secret)){
                    subscriber.onError(new Throwable("1"));
                    return;
                }
                Account account=createAccount(secret, name,password,hint);
                if (account!=null){
                    subscriber.onNext(account);
                    subscriber.onCompleted();
                    return;
                }
                subscriber.onError(new Throwable("2"));
            }
        });

        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Account>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        if ("1".equals(e.getMessage())){
                            view.displayCheckMessage("此账户已经存在");
                        }else if ("2".equals(e.getMessage())){
                            view.displayError(new Throwable("账户导入失败"));
                        }
                    }

                    @Override
                    public void onNext(Account account) {
                        AccountsManager.getInstance().addAccount(account);
                        AppConfig.putLastAccountAddress(account.getAddress());
                        view.displayImportAccountResult(true,"账户导入成功");
                    }
                });
        subscriptions.add(subscription);
    }


    private Account createAccount(String secret,String name, String passwd, String hint){

        try {
            String publicKey=AschSDK.Helper.getPublicKey(secret);
            String address=AschFactory.getInstance().getSecurity().getAddress(publicKey);
            Account account=new Account();
            account.setName(name);
            account.setPasswd(passwd);
            account.setPublicKey(publicKey);
            account.setAddress(address);
            account.setHint(hint);
            account.setSeed(secret);
            AccountSecurity.encryptAccount(account,passwd);
            account.setSeed(null);
            account.setPasswd(null);
            return account;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }


}
