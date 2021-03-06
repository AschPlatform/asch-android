package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.NodeURLSettingFragment;

/**
 * Created by kimziv on 2017/10/23.
 */

public class NodeURLSettingActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.set_node_url));

        NodeURLSettingFragment fragment =NodeURLSettingFragment.newInstance();

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

    }
}
