package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.IssuerAssets;
import asch.so.wallet.view.fragment.IssuerAssetsFragment;
import asch.so.wallet.view.fragment.TermServiceFragment;


public class IssuerAssetsActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.asset_issue));
        IssuerAssetsFragment fragment=IssuerAssetsFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fragment_container);
    }
}
