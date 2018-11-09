package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.IssuerAssetsFragment;


public class IssuerAssetsActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.asset_issue));
        IssuerAssetsFragment fragment=IssuerAssetsFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fragment_container);
    }
}
