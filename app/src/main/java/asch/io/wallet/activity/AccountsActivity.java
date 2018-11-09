package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.activity.component.DaggerAccountsManagerComponent;
import asch.io.wallet.presenter.component.DaggerPresenterComponent;
import asch.io.wallet.view.fragment.AccountsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountsActivity extends TitleToolbarActivity  {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AccountsFragment accountsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(getString(R.string.all_account));
        accountsFragment=AccountsFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), accountsFragment, R.id.fragment_container);
    }

}
