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

public class AccountsActivity extends TitleToolbarActivity implements View.OnClickListener {



    @BindView(R.id.toolbar)
    Toolbar toolbar;
    EasyPopup moreEasyPopup;
    AccountsFragment accountsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setRightTitle("");
        setTitle(getString(R.string.all_account));
        initPopupMenu();
        accountsFragment=AccountsFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), accountsFragment, R.id.fragment_container);
    }

    @Override
    protected void onRightClicked(View v) {
        showPopupMenu(v, SizeUtils.dp2px(30), SizeUtils.dp2px(-12));
    }

    private void showPopupMenu(View view, int offsetX, int offsetY) {
        moreEasyPopup.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.LEFT, offsetX,offsetY);
    }

    private void initPopupMenu() {
        moreEasyPopup = new EasyPopup(AccountsActivity.this)
                .setContentView(R.layout.menu_account)
                .setAnimationStyle(R.style.PopupMenuAnimation)
                .setFocusAndOutsideEnable(true)
                .createPopup();
        View contentView = moreEasyPopup.getContentView();
        View addItem = contentView.findViewById(R.id.add_ll);
        View importItem = contentView.findViewById(R.id.import_ll);
        addItem.setOnClickListener(this);
        importItem.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_ll) {
            moreEasyPopup.dismiss();
            accountsFragment.addAccount();
        } else if (v.getId() == R.id.import_ll) {
            moreEasyPopup.dismiss();
            accountsFragment.impAccount();
        }
    }
}
