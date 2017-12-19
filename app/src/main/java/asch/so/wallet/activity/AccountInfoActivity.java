package asch.so.wallet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.AccountInfoFragment;

/**
 * Created by kimziv on 2017/12/11.
 */

public class AccountInfoActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("个人中心");
      AccountInfoFragment fragment =  AccountInfoFragment.newInstance("","");

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
    }
}