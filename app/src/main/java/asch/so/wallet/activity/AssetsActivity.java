package asch.so.wallet.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.presenter.AssetsPresenter;
import asch.so.wallet.view.fragment.AssetsFragment;

/**
 * Created by kimziv on 2017/9/20.
 */

public class AssetsActivity extends BaseActivity {

    private AssetsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

       // AssetsFragment assetsFragment = (AssetsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
       // if (assetsFragment==null){
        AssetsFragment  assetsFragment=AssetsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),assetsFragment, R.id.fragment_container);
        //}

        presenter=new AssetsPresenter(assetsFragment);
        assetsFragment.setPresenter(presenter);



    }
}
