package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Bundle;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.presenter.AssetTransactionsPresenter;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.fragment.AssetTransactionsFragment;
import asch.so.wallet.view.fragment.AssetTransferFragment;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransactionsActivity extends BaseActivity {

    private AssetTransactionsContract.Presenter presenter;
    private AssetTransactionsFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


        Intent intent=getIntent();

        fragment=AssetTransactionsFragment.newInstance();
        Bundle bundle=new Bundle();
        bundle.putString("curreny",intent.getStringExtra("curreny"));
        bundle.putInt("precision",intent.getIntExtra("precision",0));
        fragment.setArguments(bundle);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

        presenter=new AssetTransactionsPresenter(this,fragment);
        presenter.loadTransactions();

        StatusBarUtil.immersive(this);
        //StatusBarUtil.hideNavigationBar(getWindow());
    }


}
