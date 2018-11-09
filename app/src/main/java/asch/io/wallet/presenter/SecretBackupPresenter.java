package asch.io.wallet.presenter;

import android.content.Context;

import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.SecretBackupContract;
import asch.io.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/11/27.
 */

public class SecretBackupPresenter implements SecretBackupContract.Presenter {


    private Context context;
    private SecretBackupContract.View view;


    public SecretBackupPresenter(Context context, SecretBackupContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void loadSecret() {
        Account account=getAccount();
    }

    @Override
    public void backupSecret(String secret) {
       // Account account=getAccount();
        AccountsManager.getInstance().setAccountBackup(true);
    }
}
