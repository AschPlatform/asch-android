package asch.so.wallet.cordova;

import android.text.TextUtils;

import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.widget.AllPasswdsDialog;
import rx.Observable;
import rx.Subscriber;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

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
        }else if (action.equals("deposit")){

        }else if (action.equals("withdraw")){

        }else if (action.equals("innerTransfer")){

        }else if (action.equals("setNickname")){

        }else if (action.equals("postArticle")){

        }else if (action.equals("postComment")){

        }else if (action.equals("voteArticle")){

        }else if (action.equals("likeComment")){

        }else if (action.equals("report")){

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

    /**** create observables ****/

    public Observable createDepositObservable(String dappID, String currency, long amount, String message, String secret, String secondSecret) {

        return Observable.create(new Observable.OnSubscribe<AschResult>() {

            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                AschResult result = AschSDK.Dapp.deposit(dappID, currency, amount, message, secret, secondSecret);
                if (result != null && result.isSuccessful()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(result != null ? result.getException() : new Exception("result is null"));
                }
            }
        });
    }


    public void coreDeposit(){

        long amount=0;
        String msg="";
        String secret="";
        String secondSecret="";
        Observable observable=createDepositObservable("","",amount,msg,secret,secondSecret);

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
