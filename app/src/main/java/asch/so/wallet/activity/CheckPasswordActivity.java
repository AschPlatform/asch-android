package asch.so.wallet.activity;

import android.os.Bundle;
import android.text.TextUtils;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.CheckPasswordFragment;


public class CheckPasswordActivity extends TitleToolbarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getBundle().getString("title");
        if (!TextUtils.isEmpty(title)){
            setTitle(title);
        }

        CheckPasswordFragment fragment =CheckPasswordFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
