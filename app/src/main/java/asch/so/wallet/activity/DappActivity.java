package asch.so.wallet.activity;

import android.os.Bundle;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.DappFragment;

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
