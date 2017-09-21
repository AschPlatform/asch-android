package asch.so.wallet.presenter;

import android.content.Context;

import asch.so.wallet.ApplicationModule;
import asch.so.wallet.contract.AccountsContract;
import asch.so.wallet.presenter.component.DaggerPresenterComponent;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountsPresenter implements AccountsContract.Presenter{
    private AccountsContract.View view;

    private CompositeSubscription subscription;


    public AccountsPresenter(Context context, AccountsContract.View view){

        this.view=view;
        this.subscription=new CompositeSubscription();
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

    }

    @Override
    public void unSubscribe() {

    }
}
