package asch.so.wallet.presenter;

import android.content.Context;

import javax.inject.Inject;

import asch.so.wallet.ApplicationModule;
import asch.so.wallet.contract.AccountImportContract;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.presenter.component.DaggerPresenterComponent;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2017/9/22.
 */

public class AccountImportPresenter implements AccountImportContract.Presenter {
    AccountImportContract.View view;
    CompositeSubscription subscriptions;


    @Inject
    AccountsDao accountsDao;

    @Inject
    public AccountImportPresenter(Context ctx, AccountImportContract.View view) {
        this.view = view;
        this.subscriptions=new CompositeSubscription();

        DaggerPresenterComponent.builder()
                .applicationModule(new ApplicationModule(ctx))
                .build().inject(this);
    }

    @Override
    public void subscribe() {
        importAccount();
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void importAccount() {

    }
}
