package asch.io.wallet.activity;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.contract.AssetTransactionsContract;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.util.StatusBarUtil;
import asch.io.wallet.view.fragment.AssetTransactionsFragment;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransactionsActivity extends TitleToolbarActivity {

    private AssetTransactionsContract.Presenter presenter;
    private AssetTransactionsFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String name =intent.getExtras().getString("balance");
        setTitle(name);
        fragment=AssetTransactionsFragment.newInstance();
        fragment.setArguments(intent.getExtras());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
        StatusBarUtil.immersive(this);
    }


}
