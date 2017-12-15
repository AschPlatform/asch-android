package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConfig;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountCreateContract;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.entity.Account;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.Bip39;
import so.asch.sdk.security.SecurityException;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AccountCreatePresenter implements AccountCreateContract.Presenter{
    private  final  static  String TAG=AccountCreatePresenter.class.getSimpleName();
    private AccountCreateContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;
    private String tmpSecret;

    public  AccountCreatePresenter(Context context, AccountCreateContract.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
        this.subscriptions=new CompositeSubscription();
    }

    @Override
    public void storeAccount(String seed, String name, String password, String hint) {

       Observable<Account> observable =  Observable.create(new Observable.OnSubscribe<Account>() {

            @Override
            public void call(Subscriber<? super Account> subscriber) {
                String secret=genSeed();
                if (secret==null){
                    subscriber.onError(new Throwable("1"));
                    return;
                }
                if (AccountsManager.getInstance().hasAccountForSeed(secret)){
                    subscriber.onError(new Throwable("2"));
                    return;
                }
                tmpSecret=secret;
                Account account=createAccount(secret, name,password,hint);
                if (account!=null){
                    subscriber.onNext(account);
                    subscriber.onCompleted();
                    return;
                }
                subscriber.onError(new Throwable("3"));
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
                            view.displayCheckMessage("生成账户主密码失败,请重试");
                       }else if ("2".equals(e.getMessage())){
                           view.displayCheckMessage("此账户以及存在,请重试");
                       }else if ("3".equals(e.getMessage())){
                           view.displayError(new Throwable("账户创建失败"));
                       }
                   }

                   @Override
                   public void onNext(Account account) {
                       AccountsManager.getInstance().addAccount(account);
                       AppConfig.putLastAccountAddress(account.getAddress());
                       view.displayCreateAccountResult(true,"账户创建成功", tmpSecret);
                       tmpSecret=null;
                   }
               });
       subscriptions.add(subscription);
    }


    public String genSeed() {
        try {
            String words = Bip39.getInstance().generateMnemonic(12);
            Log.i(TAG, "words:"+words);
           return words;
        }catch (Exception ex){
        }
        return null;
    }


    /**
     * 创建账户
     * @param name
     * @param passwd
     * @param hint
     */
    private Account createAccount(String secret,String name, String passwd, String hint){

        try {
            String pubKey = AschSDK.Helper.getPublicKey(secret);
            Log.d(TAG,secret+secret);
            Log.d(TAG,"pubKey:"+pubKey);
            String address = AschFactory.getInstance().getSecurity().getAddress(pubKey);

            Account account =new Account();
            account.setSeed(secret);
            account.setPublicKey(pubKey);
            account.setAddress(address);
            account.setName(name);
            account.setPasswd(passwd);
            account.setHint(hint);
            AccountSecurity.encryptAccount(account,passwd);
            account.setSeed(null);
            account.setPasswd(null);
            return account;
//            AccountsManager.getInstance().addAccount(account);
//            AppConfig.putLastAccountAddress(account.getAddress());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }
}
