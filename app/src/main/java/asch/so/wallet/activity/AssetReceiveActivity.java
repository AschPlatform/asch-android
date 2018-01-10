package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.contract.AssetReceiveContract;
import asch.so.wallet.presenter.AssetReceivePresenter;
import asch.so.wallet.presenter.AssetTransferPresenter;
import asch.so.wallet.view.fragment.AssetReceiveFragment;
import asch.so.wallet.view.fragment.AssetTransferFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetReceiveActivity extends TitleToolbarActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AssetReceiveContract.Presenter presenter;
    private AssetReceiveFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(getString(R.string.qr_receipt));
        fragment=AssetReceiveFragment.newInstance();
        Bundle bundle=getBundle();
        fragment.setArguments(bundle!=null?bundle:new Bundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }
}
