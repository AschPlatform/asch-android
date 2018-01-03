package asch.so.wallet.presenter;

import android.content.Context;

import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountInfoContract;
import asch.so.wallet.model.entity.Account;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2017/12/11.
 */

public class AccountInfoPresenter implements AccountInfoContract.Presenter {

    private Context context;
    private AccountInfoContract.View view;
    private CompositeSubscription subscriptions;

    public AccountInfoPresenter(Context context, AccountInfoContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        this.subscriptions=new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadAccountInfo() {
        this.view.displayAccountInfo(getAccount());
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
