package asch.so.wallet.presenter;

import android.content.Context;

import java.io.InvalidClassException;

import javax.inject.Inject;

import asch.so.wallet.AppConfig;
import asch.so.wallet.ApplicationModule;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountsContract;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.component.DaggerPresenterComponent;
import rx.Observable;
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
            .subscribe(accounts -> view.displaySavedAccounts(accounts));
    subscriptions.add(subscription);
}

    @Override
    public void setCurrentAccount(Account account) {
        AccountsManager.getInstance().setCurrentAccount(account);
        AppConfig.putLastAccountAddress(account.getAddress());
        AppConfig.putLastAccountPublicKey(account.getPublicKey());
    }

    @Override
    public void saveCurrentAccountToPreference(String address) throws InvalidClassException {

    }
}
