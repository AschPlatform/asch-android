package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.TermServiceFragment;

/**
 * Created by kimziv on 2017/11/13.
 */

public class RegisterIssuerAgreementActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.service_term));
        TermServiceFragment fragment=TermServiceFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fragment_container);
    }
}
