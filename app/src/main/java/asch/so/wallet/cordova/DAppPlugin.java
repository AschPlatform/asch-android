package asch.so.wallet.cordova;

import android.text.TextUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.widget.AllPasswdsDialog;

/**
 * Created by kimziv on 2017/12/27.
 */

public class DAppPlugin extends CordovaPlugin {
    public final  static String TAG= DAppPlugin.class.getSimpleName();


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if(this.cordova.getActivity().isFinishing()) return true;

        if (action.equals("authorize")) {
            authorize();
        }
        callbackContext.success();
        return true;
    }

//    http://testnet.asch.so:4096/dapps/d352263c517195a8b612260971c7af869edca305bb64b471686323817e57b2c1/
    //------------------------
    //Local Methods-- core
    //------------------------

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    public void authorize(){
        boolean hasSecondSecret=getAccount().hasSecondSecret();
        AllPasswdsDialog dialog = new AllPasswdsDialog(this.cordova.getContext(),hasSecondSecret);
        dialog.setTitle("确定进入DAPP");
        dialog.show(new AllPasswdsDialog.OnConfirmationListenner() {
            @Override
            public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                if (TextUtils.isEmpty(errMsg)){
                    AppUtil.toastError(cordova.getContext(), "test");
                }else {
                    AppUtil.toastError(cordova.getContext(), errMsg);
                }
            }
        });
    }

    public void coreDeposit(){

    }

    public void coreWithdraw(){

    }

    public void coreTransfer(){

    }

    public void coreSetNickname(){

    }

    //------------------------
    //Local Methods-- cctime
    //------------------------

    public void cctimePostArticle(){

    }

    public void cctimePostComment(){

    }

    public void cctimeVoteArticle(){

    }

    public void cctimeLikeComment(){

    }

    public void cctimeReport(){

    }
}
