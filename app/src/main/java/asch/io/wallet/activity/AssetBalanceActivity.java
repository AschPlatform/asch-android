package asch.io.wallet.activity;

import android.os.Bundle;

import asch.io.base.activity.BaseActivity;
import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.presenter.AssetBalancePresenter;
import asch.io.wallet.view.fragment.AssetBalanceFragment;

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

//        presenter=new AssetBalancePresenter(assetsFragment);
       // assetsFragment.setPresenter(presenter);



    }
}
