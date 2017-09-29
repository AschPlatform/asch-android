package asch.so.wallet.activity;

import android.os.Bundle;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.AssetTransactionsFragment;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransactionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        AssetTransactionsFragment fragment=AssetTransactionsFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);


    }


}
