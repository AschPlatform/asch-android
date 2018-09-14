package asch.so.wallet.activity;

import android.os.Bundle;
import android.text.TextUtils;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.AssetTransferFragment;
import asch.so.wallet.view.fragment.CheckPasswordFragment;


public class CheckPasswordActivity extends TitleToolbarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkTitle();
        CheckPasswordFragment fragment =CheckPasswordFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }


    protected void checkTitle(){
        String title = getBundle().getString("title");
        if (TextUtils.isEmpty(title)){
            title = getString(R.string.please_input_account_password);
        }
        //不同币种的转账
        if (title.equals(AssetTransferFragment.class.getSimpleName())){
            title = getBundle().getString("currency")+getString(R.string.transfer);
        }
        setTitle(title);
    }
}
