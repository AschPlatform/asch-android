package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.contract.AssetReceiveContract;
import asch.io.wallet.view.fragment.AssetGatewayDepositFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetGatewayDepositActivity extends TitleToolbarActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AssetReceiveContract.Presenter presenter;
    private AssetGatewayDepositFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        fragment=AssetGatewayDepositFragment.newInstance();
        Bundle bundle=getBundle();
        setTitle(bundle.getString("currency")+getString(R.string.deposit));
        fragment.setArguments(bundle!=null?bundle:new Bundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
