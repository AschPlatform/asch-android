package asch.so.wallet.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by kimziv on 2017/12/27.
 */

public class DAppPlugin extends CordovaPlugin {
    public final  static String TAG= DAppPlugin.class.getSimpleName();


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        return super.execute(action, args, callbackContext);
    }

//    http://testnet.asch.so:4096/dapps/d352263c517195a8b612260971c7af869edca305bb64b471686323817e57b2c1/
    //------------------------
    //Local Methods-- core
    //------------------------

    public void authenticate(){

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
