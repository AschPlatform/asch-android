package asch.so.wallet.activity;

import android.os.Bundle;
import android.text.TextUtils;

import org.apache.cordova.CordovaActivity;

import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.view.widget.AuthorizeDialog;
import asch.so.wallet.view.widget.DAppPasswordDialog;

/**
 * Created by kimziv on 2017/12/29.
 */

public class BaseCordovaActivity extends CordovaActivity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }
        String launchUrl=extras.getString("url");
        String address=getAccount().getAddress();//extras.getString("address");
        loadUrl(String.format("%s?address=%s",launchUrl,address));
        showAuthorizeDialog();
    }

    private void showAuthorizeDialog(){
        AuthorizeDialog dialog = new AuthorizeDialog(this);
        dialog.setIcon(R.mipmap.icon_cctime);
        dialog.setHint(getString(R.string.dapp_authorize_alert));
        dialog.show(new AuthorizeDialog.OnConfirmationListenner() {
            @Override
            public void onAuthorize(AuthorizeDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onCancel(AuthorizeDialog dialog) {
                finish();
            }
        });
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
