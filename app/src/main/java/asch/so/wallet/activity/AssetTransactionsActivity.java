package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.presenter.AssetTransactionsPresenter;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.fragment.AssetTransactionsFragment;
import asch.so.wallet.view.fragment.AssetTransferFragment;
import so.asch.sdk.impl.AschConst;

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
        String currency=intent.getStringExtra("curreny");
        int precision=intent.getIntExtra("precision",0);
        String json = intent.getStringExtra("balance");

        setTitle(currency);

        fragment=AssetTransactionsFragment.newInstance();
        Bundle bundle=new Bundle();
        bundle.putString("curreny",currency);
        bundle.putInt("precision",precision);
        bundle.putString("balance", json);
        fragment.setArguments(bundle);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

        presenter=new AssetTransactionsPresenter(this,fragment);
        presenter.loadTransactions(currency, !AschConst.CORE_COIN_NAME.equals(currency));


        StatusBarUtil.immersive(this);
        //StatusBarUtil.hideNavigationBar(getWindow());
    }


}
