package asch.so.wallet.activity;

import android.os.Bundle;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.SetWalletPwdFragment;

/**
 * Created by PanD on 2018/8/20.
 */

public class SetWalletPwdActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.set_wallet_pwd));
        SetWalletPwdFragment fragment =SetWalletPwdFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
