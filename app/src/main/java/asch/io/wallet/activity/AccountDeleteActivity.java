package asch.io.wallet.activity;

import android.os.Bundle;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.AccountDeleteFragment;


public class AccountDeleteActivity extends TitleToolbarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.delete_account));
        AccountDeleteFragment fragment =AccountDeleteFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
