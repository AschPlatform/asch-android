package asch.io.wallet.activity;

import android.os.Bundle;
import android.text.TextUtils;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.AssetBalanceFragment;
import asch.io.wallet.view.fragment.AssetTransactionsFragment;
import asch.io.wallet.view.fragment.AssetTransferFragment;
import asch.io.wallet.view.fragment.AssetWithdrawFragment;
import asch.io.wallet.view.fragment.CheckPasswordFragment;
import asch.io.wallet.view.fragment.IssueAssetFragment;
import asch.io.wallet.view.fragment.RegisterIssuerFragment;
import asch.io.wallet.view.fragment.RegisterUIAAssetFragment;


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
        else if(title.equals(AssetTransactionsFragment.class.getSimpleName())){
            title = getBundle().getString("currency")+getString(R.string.deposit);
        }
        else if (title.equals(AssetTransferFragment.class.getSimpleName())){
            title = getBundle().getString("currency")+getString(R.string.transfer);
        }
        else if(title.equals(AssetWithdrawFragment.class.getSimpleName())){
            title = getBundle().getString("currency")+getString(R.string.withdraw);
        }
        else if (title.equals(AccountsActivity.class.getSimpleName())||title.equals(AssetBalanceFragment.class.getSimpleName())){
            title = getString(R.string.import_account);
        }else if (title.equals(RegisterIssuerFragment.class.getSimpleName())){
            title = getString(R.string.register_issuer);
        }else if (title.equals(RegisterUIAAssetFragment.class.getSimpleName())){
            title = getString(R.string.register_new_asset);
        }else if(title.equals(IssueAssetFragment.class.getSimpleName())){
            title = getString(R.string.issue_asset);
        }


        setTitle(title);
    }
}
