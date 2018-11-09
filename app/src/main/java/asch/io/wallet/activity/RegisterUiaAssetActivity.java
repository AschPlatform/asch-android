package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.RegisterUIAAssetFragment;


public class RegisterUiaAssetActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.register_new_asset));
        RegisterUIAAssetFragment fragment = RegisterUIAAssetFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
