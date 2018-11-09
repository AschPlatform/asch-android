package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.LockCoinsFragment;

/**
 * Created by kimziv on 2017/12/13.
 */

public class LockCoinsActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.locked));
        LockCoinsFragment fragment =LockCoinsFragment.newInstance("","");
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
    }
}
