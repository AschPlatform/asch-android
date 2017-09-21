package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.WalletApplication;
import asch.so.wallet.activity.component.AccountsManagerComponent;
import asch.so.wallet.activity.component.DaggerAccountsManagerComponent;
import asch.so.wallet.activity.module.AccountsModule;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountsPresenter;
import asch.so.wallet.presenter.component.DaggerPresenterComponent;
import asch.so.wallet.view.fragment.AccountsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    AccountsPresenter accountsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        AccountsFragment accountsFragment=AccountsFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), accountsFragment, R.id.fragment_container);

        DaggerAccountsManagerComponent.builder()
                .applicationComponent(WalletApplication.getInstance().getApplicationComponent())
                .accountsModule(new AccountsModule(accountsFragment))
                .build().inject(this);


    }
}
