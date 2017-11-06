package asch.so.wallet.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.presenter.AssetBalancePresenter;
import asch.so.wallet.presenter.AssetBalancePresenter;
import asch.so.wallet.view.fragment.AssetBalanceFragment;

/**
 * Created by kimziv on 2017/9/20.
 */

public class AssetBalanceActivity extends BaseActivity {

    private AssetBalancePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

       // AssetsFragment assetsFragment = (AssetsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
       // if (assetsFragment==null){
        AssetBalanceFragment assetsFragment=AssetBalanceFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),assetsFragment, R.id.fragment_container);
        //}

        presenter=new AssetBalancePresenter(assetsFragment);
        assetsFragment.setPresenter(presenter);



    }
}