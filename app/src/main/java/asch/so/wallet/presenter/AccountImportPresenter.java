package asch.so.wallet.presenter;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

import asch.so.wallet.AppConfig;
import asch.so.wallet.ApplicationModule;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountImportContract;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.component.DaggerPresenterComponent;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.SecurityException;

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

//        DaggerPresenterComponent.builder()
//                .applicationModule(new ApplicationModule(ctx))
//                .build().inject(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void importAccount(String seed, String name, String password, String hint) {
        try {
            String publicKey=AschSDK.Helper.getPublicKey(seed);
            String address=AschFactory.getInstance().getSecurity().getAddress(publicKey);
            Account account=new Account();
            account.setName(name);
            account.setPasswd(password);
            account.setPublicKey(publicKey);
            account.setAddress(address);
            account.setHint(hint);
            account.setSeed(seed);
            AccountSecurity.encryptAccount(account,password);
            AccountsManager.getInstance().addAccount(account);
            AppConfig.putLastAccountAddress(account.getAddress());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
