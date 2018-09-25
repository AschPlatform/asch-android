package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.SizeUtils;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import javax.inject.Inject;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseDialogFragment;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.WalletApplication;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.activity.component.AccountsManagerComponent;
import asch.so.wallet.activity.component.DaggerAccountsManagerComponent;
import asch.so.wallet.activity.module.AccountsModule;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountsPresenter;
import asch.so.wallet.presenter.component.DaggerPresenterComponent;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.fragment.AccountsFragment;
import asch.so.wallet.view.widget.InputPasswdDialog;
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
