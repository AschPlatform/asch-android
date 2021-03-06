package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.DAppDetailFragment;

/**
 * Created by kimziv on 2018/1/31.
 */

public class DAppDetailActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.dapp_detail));
        Bundle bundle=  getBundle();
        //DAppDetailFragment fragment = DAppDetailFragment.newInstance(bundle.getString("dapp_id"));
        DAppDetailFragment fragment = DAppDetailFragment.newInstance(bundle);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
    }


}
