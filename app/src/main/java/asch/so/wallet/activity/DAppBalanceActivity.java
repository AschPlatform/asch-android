package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.DAppBalanceFragment;
import asch.so.wallet.view.fragment.PeersFragment;

/**
 * Created by kimziv on 2018/2/9.
 */

public class DAppBalanceActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.dapp_balance_detail));
        String dappId = getBundle().getString("dapp_id");
        DAppBalanceFragment fragment=DAppBalanceFragment.newInstance(dappId);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
    }
}
