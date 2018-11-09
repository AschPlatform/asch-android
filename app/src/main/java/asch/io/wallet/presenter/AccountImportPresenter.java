package asch.io.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import asch.io.base.view.Throwable;
import asch.io.wallet.AppConfig;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.contract.AccountImportContract;
import asch.io.wallet.crypto.AccountSecurity;
import asch.io.wallet.model.db.dao.AccountsDao;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.model.entity.FullAccount;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    private  Observable<Account> createNewAccountObservable(String secret, String name){
      return  Observable.create(new Observable.OnSubscribe<Account>() {

            @Override
            public void call(Subscriber<? super Account> subscriber) {
                if (AccountsManager.getInstance().hasAccountForSeed(secret)){
                    subscriber.onError(new Throwable("1"));
                    return;
                }
                Account account=createAccount(secret, name);
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
       // String address=account.getAddress();
        String publicKey=account.getPublicKey();
        return rx.Observable.create(new rx.Observable.OnSubscribe<Account>() {
            @Override
            public void call(Subscriber<? super Account> subscriber) {
                try {
                   String address=AschFactory.getInstance().getSecurity().getAddress(publicKey);
                    AschResult result = AschSDK.Account.getAccountV2(address);
                    if (result != null && result.isSuccessful()) {
                        LogUtils.iTag(TAG, result.getRawJson());
                        FullAccount fullAccount = JSON.parseObject(result.getRawJson(), FullAccount.class);
                        account.setFullAccount(fullAccount);


                        if (fullAccount.getAccount()==null){
                            FullAccount.AccountInfo accountInfo = new FullAccount.AccountInfo();
                            accountInfo.setAddress(address);
                            accountInfo.setPublicKey(publicKey);
                            accountInfo.setBalance("0");
                            fullAccount.setAccount(accountInfo);
                        }
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
    public void importAccount(String secret, String name) {
        Observable<Account> newAccountObservable = createNewAccountObservable(secret,name);
        //rx.Observable<FullAccount> loginObservable = AccountsManager.getInstance().createLoginAccountObservable("");
        Observable<Account> flatMapObservable =  newAccountObservable.flatMap(new Func1<Account, Observable<Account>>() {
            @Override
            public Observable<Account> call(Account account) {
              return  createLoginAccountObservable(account);
            }
        });

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

                        AccountsManager.getInstance().addAccount(account);
                        AssetManager.getInstance().loadAccountAssets();
                        loadBalance();
                        AppConfig.putLastAccountPublicKey(account.getPublicKey());
                        view.displayImportAccountResult(true,context.getString(R.string.import_success));
                    }
                });
        subscriptions.add(subscription);
    }

    void loadBalance(){
        rx.Observable balanceAndAssetsObservable = AssetManager.getInstance().balanceAndAssetsObservable(AccountsManager.getInstance().getCurrentAccount().getAddress());
        Subscription subscription2= balanceAndAssetsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LinkedHashMap<String,AschAsset>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {

                    }

                    @Override
                    public void onNext(LinkedHashMap<String,AschAsset> assetsMap) {

                        AssetManager.getInstance().saveBalanceAndAsset(assetsMap);
                    }
                });
        subscriptions.add(subscription2);
    }


    private Account createAccount(String secret,String name){

        try {
            String publicKey=AschSDK.Helper.getPublicKey(secret);
            String address=AschFactory.getInstance().getSecurity().getAddress(publicKey);
            Account account=new Account();
            account.setName(name);

            account.setPublicKey(publicKey);
            account.setAddress(address);

            account.setSeed(secret);
            AccountSecurity.encryptAccount(account);
            account.setSeed(null);

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
