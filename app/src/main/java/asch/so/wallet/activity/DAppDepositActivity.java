package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.DAppDepositFragment;

/**
 * Created by kimziv on 2018/2/11.
 */

public class DAppDepositActivity extends TitleToolbarActivity{

    private DAppDepositFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.dapp_deposit));
        toolbar.setRightVisible(false);
        fragment= DAppDepositFragment.newInstance();
        Bundle bundle=getIntent().getExtras();
        fragment.setArguments(bundle);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }

    @Override
    protected void onRightClicked(View v) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
