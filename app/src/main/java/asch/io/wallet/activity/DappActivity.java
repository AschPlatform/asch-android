package asch.io.wallet.activity;

import android.os.Bundle;

import asch.io.base.activity.BaseActivity;
import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.DappFragment;

/**
 * Created by kimziv on 2017/9/20.
 */

public class DappActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // AssetsFragment assetsFragment = (AssetsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        // if (assetsFragment==null){
        DappFragment dappFragment=DappFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),dappFragment, R.id.fragment_container);
        //}

//        presenter=new AssetsPresenter(assetsFragment);
//        presenter.loadAssets();
    }
}
