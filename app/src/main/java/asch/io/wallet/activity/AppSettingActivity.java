package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.AppSettingFragment;

/**
 * Created by kimziv on 2017/10/23.
 */

public class AppSettingActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_base);
        setTitle(getString(R.string.system_set));
        AppSettingFragment fragment = AppSettingFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
