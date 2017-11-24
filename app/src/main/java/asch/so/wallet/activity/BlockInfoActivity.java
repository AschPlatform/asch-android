package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.FullAccount;
import asch.so.wallet.view.fragment.BlockInfoFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.AschHelper;
import so.asch.sdk.AschSDK;
import so.asch.sdk.Block;

/**
 * Created by kimziv on 2017/11/24.
 */

public class BlockInfoActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("区块信息");

        BlockInfoFragment fragment =BlockInfoFragment.newInstance();

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }

}
