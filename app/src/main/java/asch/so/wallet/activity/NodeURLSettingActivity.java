package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.view.fragment.NodeURLSettingFragment;

/**
 * Created by kimziv on 2017/10/23.
 */

public class NodeURLSettingActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置节点URL");

        NodeURLSettingFragment fragment =NodeURLSettingFragment.newInstance();

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

    }
}
