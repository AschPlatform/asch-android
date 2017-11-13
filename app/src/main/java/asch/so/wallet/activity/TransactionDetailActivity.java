package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.TransactionDetailFragment;

/**
 * Created by kimziv on 2017/10/27.
 */

public class TransactionDetailActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("交易详情");
        TransactionDetailFragment fragment =TransactionDetailFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
    }
}
