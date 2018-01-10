package asch.so.wallet.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConfig;
import asch.so.wallet.AppConstants;
import asch.so.wallet.ApplicationModule;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountImportContract;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.FullAccount;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
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
    private Context context;


    @Inject
    AccountsDao accountsDao;

    @Inject
    public AccountImportPresenter(Context ctx, AccountImportContract.View view) {
        this.context = ctx;
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

    private  Observable<Account> createNewAccountObservable(String secret, String name, String password, String hint){
      return  Observable.create(new Observable.OnSubscribe<Account>() {

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

    }

    public  rx.Observable<Account> createLoginAccountObservable(Account account){
        String publicKey=account.getPublicKey();
        return rx.Observable.create(new rx.Observable.OnSubscribe<Account>() {
            @Override
            public void call(Subscriber<? super Account> subscriber) {
                try {
                    AschResult result = AschSDK.Account.secureLogin(publicKey);
                    if (result != null && result.isSuccessful()) {
                        LogUtils.iTag(TAG, result.getRawJson());
                        FullAccount fullAccount = JSON.parseObject(result.getRawJson(), FullAccount.class);
                        account.setFullAccount(fullAccount);
                        subscriber.onNext(account);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(result.getException());
                    }
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        });
    }

    @Override
    public void importAccount(String secret, String name, String password, String hint) {
        Observable<Account> newAccoutObservable = createNewAccountObservable(secret,name,password,hint);
        //rx.Observable<FullAccount> loginObservable = AccountsManager.getInstance().createLoginAccountObservable("");
        Observable<Account> flatMapObservable =  newAccoutObservable.flatMap(new Func1<Account, Observable<Account>>() {
            @Override
            public Observable<Account> call(Account account) {
              return  createLoginAccountObservable(account);
            }
        });


        //                Observable.create(new Observable.OnSubscribe<Account>() {
//
//            @Override
//            public void call(Subscriber<? super Account> subscriber) {
//                if (AccountsManager.getInstance().hasAccountForSeed(secret)){
//                    subscriber.onError(new Throwable("1"));
//                    return;
//                }
//                Account account=createAccount(secret, name,password,hint);
//                if (account!=null){
//                    subscriber.onNext(account);
//                    subscriber.onCompleted();
//                    return;
//                }
//                subscriber.onError(new Throwable("2"));
//            }
//        });

//        Subscription subscription = observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<Account>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(java.lang.Throwable e) {
//                        if ("1".equals(e.getMessage())){
//                            view.displayCheckMessage("此账户已经存在");
//                        }else if ("2".equals(e.getMessage())){
//                            view.displayError(new Throwable("账户导入失败"));
//                        }
//                    }
//
//                    @Override
//                    public void onNext(Account account) {
//                        AccountsManager.getInstance().addAccount(account);
////                        AppConfig.putLastAccountAddress(account.getAddress());
//                        AppConfig.putLastAccountPublicKey(account.getPublicKey());
//                        view.displayImportAccountResult(true,"账户导入成功");
//                    }
//                });

        Subscription subscription = flatMapObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Account>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        if ("1".equals(e.getMessage())){
                            view.displayCheckMessage(context.getString(R.string.accounts_exist));
                        }else if ("2".equals(e.getMessage())){
                            view.displayError(new Throwable(context.getString(R.string.import_fail)));
                        }else {
                            view.displayError(new Throwable(context.getString(R.string.net_error)));
                        }
                    }

                    @Override
                    public void onNext(Account account) {
                        String address=account.getFullAccount().getAccount().getAddress();
                        if (!TextUtils.isEmpty(address)){
                            account.setAddress(address);
                        }
                        AccountsManager.getInstance().addAccount(account);
//                        AppConfig.putLastAccountAddress(account.getAddress());
                        AppConfig.putLastAccountPublicKey(account.getPublicKey());
                        view.displayImportAccountResult(true,context.getString(R.string.import_success));
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


//    public void loadAccount(String pubKey) {
//        if (TextUtils.isEmpty(pubKey)){
//            return;
//        }
//        rx.Observable<FullAccount> observable = AccountsManager.getInstance().createLoginAccountObservable(pubKey);
//        Subscription subscription = observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<FullAccount>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(java.lang.Throwable e) {
//                        LogUtils.dTag("xasObservable error:", e.toString());
//                        view.displayError(new Throwable("网络错误"));
//                    }
//
//                    @Override
//                    public void onNext(FullAccount fullAccount) {
//                        //view.displayBlockInfo(fullAccount);
//                    }
//                });
//        subscriptions.add(subscription);
//    }


}
