package asch.so.wallet.activity;

import android.os.Bundle;
import android.text.TextUtils;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.AccountBackUpCheckOrderFragment;
import asch.so.wallet.view.fragment.CheckPasswordFragment;


public class AccountBackUpCheckOrderActivity extends TitleToolbarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.account_backup));
        AccountBackUpCheckOrderFragment fragment =AccountBackUpCheckOrderFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
