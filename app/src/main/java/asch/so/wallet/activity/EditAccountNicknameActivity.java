package asch.so.wallet.activity;

import android.os.Bundle;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.contract.AccountCreateContract;
import asch.so.wallet.view.fragment.EditAccountRemarkFragment;


/**
 * Created by kimziv on 2017/9/22.
 */

public class EditAccountNicknameActivity extends TitleToolbarActivity {

    private AccountCreateContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.edit_account_remark));
        EditAccountRemarkFragment fragment =EditAccountRemarkFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

    }
}
