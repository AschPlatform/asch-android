package asch.so.wallet.activity;

import android.os.Bundle;
import android.text.TextUtils;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.CheckPasswordFragment;
import asch.so.wallet.view.fragment.SecondCheckPasswordFragment;


public class SecondCheckPasswordActivity extends CheckPasswordActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkTitle();
        SecondCheckPasswordFragment fragment = SecondCheckPasswordFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
