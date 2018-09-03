package asch.so.wallet.activity;

import android.os.Bundle;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.AccountBackUpAttentionFragment;
import asch.so.wallet.view.fragment.AccountBackUpShowMnemonicFragment;


public class AccountBackUpShowMnemonicActivity extends TitleToolbarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.account_backup));
        AccountBackUpShowMnemonicFragment fragment =AccountBackUpShowMnemonicFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
