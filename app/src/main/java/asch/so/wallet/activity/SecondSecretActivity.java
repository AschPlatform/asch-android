package asch.so.wallet.activity;

import android.os.Bundle;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.contract.AccountCreateContract;
import asch.so.wallet.view.fragment.SecondSecretFragment;

/**
 * Created by haizeiwang on 2018/1/22.
 */

public class SecondSecretActivity extends TitleToolbarActivity {



    private AccountCreateContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.set_second_secret));

        SecondSecretFragment fragment = SecondSecretFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);

    }


}
