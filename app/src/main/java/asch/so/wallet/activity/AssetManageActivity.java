package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.AccountsFragment;
import asch.so.wallet.view.fragment.AssetManageFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AssetManageActivity extends TitleToolbarActivity  {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AssetManageFragment assetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(getString(R.string.add_asset));
        assetFragment=AssetManageFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), assetFragment, R.id.fragment_container);
    }


    @Override
    public void finish() {
        if (assetFragment.isModify)
            setResult(1);
        super.finish();
    }
}
