package asch.io.wallet.presenter;

import android.content.Context;

import asch.io.base.view.Throwable;
import asch.io.wallet.R;
import asch.io.wallet.accounts.Wallet;
import asch.io.wallet.contract.SetWalletPwdContract;
import asch.io.wallet.crypto.AccountSecurity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class SetWalletPwdPresenter implements SetWalletPwdContract.Presenter{
    private  final  static  String TAG=SetWalletPwdPresenter.class.getSimpleName();
    private SetWalletPwdContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;
    private String tmpSecret;

    public SetWalletPwdPresenter(Context context, SetWalletPwdContract.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
        this.subscriptions=new CompositeSubscription();
    }

    @Override
    public void createWallet(String password) {

       Observable<Wallet> observable =  Observable.create(new Observable.OnSubscribe<Wallet>() {
            @Override
            public void call(Subscriber<? super Wallet> subscriber) {
                Wallet wallet=saveWallet(password);
                if (wallet!=null){
                    subscriber.onNext(wallet);
                    subscriber.onCompleted();
                    return;
                }
                subscriber.onError(new Throwable("1"));
            }
        });

      Subscription subscription = observable.subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .unsubscribeOn(Schedulers.io())
               .subscribe(new Subscriber<Wallet>() {
                   @Override
                   public void onCompleted() {

                   }

                   @Override
                   public void onError(java.lang.Throwable e) {
                       if ("1".equals(e.getMessage())){
                           view.displayError(new Throwable(context.getString(R.string.accounts_create_fail)));
                       }
                   }

                   @Override
                   public void onNext(Wallet wallet) {

                       view.displayCreateAccountResult(true,context.getString(R.string.account_create_success), tmpSecret);
                       tmpSecret=null;
                   }
               });
       subscriptions.add(subscription);
    }




    private Wallet saveWallet(String passwd){
        Wallet wallet = Wallet.getInstance();
        AccountSecurity.encryptWallet(wallet,passwd);
        return wallet;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }
}
