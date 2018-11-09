package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.AccountInfoFragment;

/**
 * Created by kimziv on 2017/12/11.
 */

public class AccountInfoActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.personal_center));
      AccountInfoFragment fragment =  AccountInfoFragment.newInstance("","");

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
    }
}
