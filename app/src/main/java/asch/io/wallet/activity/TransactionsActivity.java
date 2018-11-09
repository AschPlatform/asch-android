package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.contract.TransactionsContract;
import asch.io.wallet.presenter.TransactionsPresenter;
import asch.io.wallet.view.fragment.TransactionsFragment;

/**
 * Created by kimziv on 2017/10/18.
 */

public class TransactionsActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.transaction_record));
        //StatusBarUtil.immersive(this);
        TransactionsFragment transactionsFragment=TransactionsFragment.newInstance();

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),transactionsFragment, R.id.fragment_container);

        TransactionsContract.Presenter presenter =new TransactionsPresenter(this, transactionsFragment);
       // presenter.loadTransactions();
    }
}
