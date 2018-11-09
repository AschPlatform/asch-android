package asch.io.wallet.presenter;

import android.content.Context;

import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.AccountDetailContract;
import asch.io.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/10/31.
 */

public class AccountDetailPresenter implements AccountDetailContract.Presenter {


    Context context;
    AccountDetailContract.View view;

    public AccountDetailPresenter(Context context, AccountDetailContract.View view) {
        this.context=context;
        this.view=view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public Account getAccount(){

        return  AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void loadAccount(String address) {
        this.view.displayAccount(getAccount());
    }

    @Override
    public void changeAccountName(String name) {
        AccountsManager.getInstance().updateAccount(name);

    }
}
