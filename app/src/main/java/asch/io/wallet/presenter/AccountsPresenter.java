package asch.io.wallet.presenter;

import android.content.Context;

import java.io.InvalidClassException;

import javax.inject.Inject;

import asch.io.wallet.AppConfig;
import asch.io.wallet.ApplicationModule;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.AccountsContract;
import asch.io.wallet.model.db.dao.AccountsDao;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.presenter.component.DaggerPresenterComponent;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountsPresenter implements AccountsContract.Presenter{
    private AccountsContract.View view;

    private CompositeSubscription subscriptions;

    @Inject
    AccountsDao accountsDao;


    @Inject
    public AccountsPresenter(Context context, AccountsContract.View view){

        this.view=view;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);
        DaggerPresenterComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .build().inject(this);
//        DaggerPresenterComponent.builder()
//                .applicationModule(new ApplicationModule(context))
//                .build().inject(this);
    }

    @Override
    public void subscribe() {
        loadSavedAccounts();
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

//    @Override
//    public void loadSavedAccounts() {
//        Subscription subscription= accountsDao.queryAllSavedAccounts()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(accounts->view.displaySavedAccounts(accounts));
//        subscriptions.add(subscription);
//    }
    @Override
    public void loadSavedAccounts() {
//    Subscription subscription= accountsDao.queryAllSavedAccounts()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(accounts->view.displaySavedAccounts(accounts));
//    subscriptions.add(subscription);

        Subscription subscription = Observable.just(accountsDao.queryAllSavedAccounts())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<RealmResults<Account>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RealmResults<Account> accounts) {
                        view.displaySavedAccounts(accounts);
                    }
                });
               // .subscribe(accounts -> view.displaySavedAccounts(accounts));
        subscriptions.add(subscription);
}

    @Override
    public void setCurrentAccount(Account account) {
        AccountsManager.getInstance().setCurrentAccount(account);

        //AppConfig.putLastAccountAddress(account.getAddress());
        AppConfig.putLastAccountPublicKey(account.getPublicKey());
    }

    @Override
    public void saveCurrentAccountToPreference(String address) throws InvalidClassException {

    }
}
