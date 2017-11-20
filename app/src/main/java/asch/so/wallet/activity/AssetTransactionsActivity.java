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
        String json =intent.getExtras().getString("balance");
        Balance balance=JSON.parseObject(json,Balance.class);
        setTitle(balance.getCurrency());

        fragment=AssetTransactionsFragment.newInstance();
        fragment.setArguments(intent.getExtras());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

//        presenter=new AssetTransactionsPresenter(this,fragment, balance.getCurrency());
//        presenter.loadTransactions(balance.getCurrency(), !AschConst.CORE_COIN_NAME.equals(balance.getCurrency()));


        StatusBarUtil.immersive(this);
    }


}
