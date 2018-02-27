package asch.so.wallet.cordova;

import android.text.TextUtils;
import android.util.Log;

import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.widget.AllPasswdsDialog;
import asch.so.wallet.view.widget.DAppPasswordDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/12/27.
 */

public class DAppPlugin extends BasePlugin {
    public final  static String TAG= DAppPlugin.class.getSimpleName();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(this.cordova.getActivity().isFinishing()) return true;

        if (DAppContract.CoreDeposit.getName().equals(action)){
            String dappID=args.getString(0);
            String currency=args.getString(1);
            long amount=args.getLong(2);
            String message =args.getString(3);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                            //暂时用不到
                           // deposit(dappID,currency,amount,message,callbackContext);
                        }
                    });
                }
            });
        }else if (DAppContract.CoreWithdrawal.getName().equals(action)){
            String dappID=args.getString(0);
            String currency=args.getString(1);
            long amount=args.getLong(2);
            String message =args.getString(3);
            long fee=args.getLong(4);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                           coreWithdraw(dappID,fee,currency,amount,message,secret,callbackContext);
                        }
                    });
                }
            });
        }else if (DAppContract.CoreTransfer.getName().equals(action)){
            String dappID=args.getString(0);
            String currency=args.getString(1);
            String targetAddress=args.getString(2);
            long amount=args.getLong(3);
            String message =args.getString(4);
            long fee=args.getLong(5);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                           coreTransfer(dappID,fee,currency,targetAddress,amount,message,secret,callbackContext);
                        }
                    });
                }
            });
        }else if (DAppContract.CoreSetNickname.getName().equals(action)){
            String dappID=args.getString(0);
            String nickname=args.getString(1);
            long fee=args.getLong(2);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                               coreSetNickname(dappID,fee,nickname,secret,callbackContext);
                        }
                    });
                }
            });
        }
        return true;
    }

//    http://testnet.asch.so:4096/dapps/d352263c517195a8b612260971c7af869edca305bb64b471686323817e57b2c1/
    //------------------------
    //Local Methods-- core
    //------------------------


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
                    subscriber.onError(result != null ? result.getException() : new Throwable("result is null"));
                }
            }
        });
    }




    private void deposit(String dappID,long fee, String currency, long amount, String message, String secret, String secondSecret,CallbackContext callbackContext){
        coreDeposit(dappID,currency,amount,message,secret,secondSecret,callbackContext);
    }

    private void coreWithdraw(String dappID, long fee, String currency, long amount, String message, String secret, CallbackContext callbackContext){

        String[] args={currency,String.valueOf(amount)};
        invokeContract(dappID, DAppContract.CoreWithdrawal.getType(),fee, args,secret, callbackContext);
    }


    private void coreTransfer(String dappID, long fee,String currency, String targetAddress, long amount, String message, String secret,CallbackContext callbackContext){
        String[] args={currency,String.valueOf(amount),targetAddress};
        invokeContract(dappID, DAppContract.CoreTransfer.getType(),fee, args,secret, callbackContext);
    }

    private void coreSetNickname(String dappID,long fee, String nickname, String secret, CallbackContext callbackContext){
        String[] args={nickname};
        invokeContract(dappID, DAppContract.CoreSetNickname.getType(),fee, args,secret, callbackContext);
    }


    public void coreDeposit(String dappID, String currency, long amount, String message, String secret, String secondSecret, CallbackContext callbackContext){
//
        Observable observable=createDepositObservable(dappID,currency,amount,message,secret,secondSecret);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AppUtil.toastError(cordova.getContext(), "充值失败");
                        //callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    }

                    @Override
                    public void onNext(AschResult aschResult) {
                        String rawJson = aschResult.getRawJson();
                        Log.i(TAG, "+++++++" + rawJson);
                       // callBack.onCallBack(rawJson);
                       // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, aschResult.getRawJson()));
                        AppUtil.toastSuccess(cordova.getContext(), "充值成功");
                        dismissDialog();

                    }
                });
    }
}
