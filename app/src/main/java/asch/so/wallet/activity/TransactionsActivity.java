package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.contract.TransactionsContract;
import asch.so.wallet.presenter.TransactionsPresenter;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.fragment.AssetBalanceFragment;
import asch.so.wallet.view.fragment.TransactionsFragment;

/**
 * Created by kimziv on 2017/10/18.
 */

public class TransactionsActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("交易记录");
        //StatusBarUtil.immersive(this);
        TransactionsFragment transactionsFragment=TransactionsFragment.newInstance();

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),transactionsFragment, R.id.fragment_container);

        TransactionsContract.Presenter presenter =new TransactionsPresenter(this, transactionsFragment);
       // presenter.loadTransactions();
    }
}
