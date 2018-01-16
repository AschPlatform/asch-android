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

public class DAppPlugin extends CordovaPlugin {
    public final  static String TAG= DAppPlugin.class.getSimpleName();
    private AllPasswdsDialog dialog;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if(this.cordova.getActivity().isFinishing()) return true;

        if (action.equals("authorize")) {
            authorize();
        }else if (action.equals("deposit")){
            String dappID=args.getString(0);
            String currency=args.getString(1);
            long amount=args.getLong(2);
            String message =args.getString(3);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    deposit(dappID,currency,amount,message,callbackContext);
                }
            });
        }else if (action.equals("withdraw")){
            String dappID=args.getString(0);
            String currency=args.getString(1);
            long amount=args.getLong(2);
            String message =args.getString(3);
            //long fee=args.getLong(4);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    withdraw(dappID,currency,amount,message,callbackContext);
                }
            });
        }else if (action.equals("innerTransfer")){//currency, amount, fee, message, targetAddress
            String dappID=args.getString(0);
            String currency=args.getString(1);
            String targetAddress=args.getString(2);
            long amount=args.getLong(3);
            String message =args.getString(4);
            long fee=args.getLong(5);


            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    transfer(dappID,currency,targetAddress,amount,message,fee,callbackContext);
                }
            });
        }else if (action.equals("setNickname")){

            String dappID=args.getString(0);
            String nickname=args.getString(1);
            long fee=args.getLong(2);


            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setNickname(dappID, nickname, fee, callbackContext);
                }
            });

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
                    subscriber.onError(result != null ? result.getException() : new Throwable("result is null"));
                }
            }
        });
    }


    public Observable createWithdrawObservable(String dappID, String currency, long amount, String message, String secret, String secondSecret) {

        return Observable.create(new Observable.OnSubscribe<AschResult>() {

            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                AschResult result = AschSDK.Dapp.withdraw(dappID, currency, amount, AschConst.Fees.DAPP_WITHDRAW, secret, secondSecret);
                if (result != null && result.isSuccessful()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(result != null ? result.getException() : new Throwable("result is null"));
                }
            }
        });
    }

    public Observable createTransferObservable(String dappID, String currency, String targetAddress, long amount, String message, long fee, String secret, String secondSecret) {

        return Observable.create(new Observable.OnSubscribe<AschResult>() {

            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                AschResult result = AschSDK.Dapp.innerTransfer(dappID, currency, targetAddress, amount, AschConst.Fees.DAPP_TRANSFER,message, secret, secondSecret);
                if (result != null && result.isSuccessful()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(result != null ? result.getException() : new Throwable("result is null"));
                }
            }
        });
    }

    public Observable createSetNicknameObservable(String dappID, String nickname, long fee, String secret, String secondSecret) {

        return Observable.create(new Observable.OnSubscribe<AschResult>() {

            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                AschResult result = AschSDK.Dapp.setNickname(dappID, nickname, fee, secret, secondSecret);
                if (result != null && result.isSuccessful()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(result != null ? result.getException() : new Throwable("result is null"));
                }
            }
        });
    }



    private void deposit(String dappID, String currency, long amount, String message, CallbackContext callbackContext){

        showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
            @Override
            public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                if (TextUtils.isEmpty(errMsg)){
                    coreDeposit(dappID,currency,amount,message,secret,secondSecret,callbackContext);
                }else {
                    // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    AppUtil.toastError(cordova.getContext(), "账户密码错误");

                }
            }
        });
    }

    private void withdraw(String dappID, String currency, long amount, String message, CallbackContext callbackContext){

        showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
            @Override
            public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                if (TextUtils.isEmpty(errMsg)){
                    coreWithdraw(dappID,currency,amount,message,secret,secondSecret,callbackContext);
                }else {
                    // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    AppUtil.toastError(cordova.getContext(), "账户密码错误");

                }
            }
        });
    }


    private void transfer(String dappID, String currency, String targetAddress, long amount, String message, long fee, CallbackContext callbackContext){

        showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
            @Override
            public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                if (TextUtils.isEmpty(errMsg)){
                    coreTransfer(dappID,currency, targetAddress,amount, message, fee, secret, secondSecret, callbackContext);
                }else {
                    // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    AppUtil.toastError(cordova.getContext(), "账户密码错误");

                }
            }
        });
    }

    private void setNickname(String dappID, String nickname, long fee, CallbackContext callbackContext){

        showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
            @Override
            public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                if (TextUtils.isEmpty(errMsg)){
                    coreSetNickname(dappID,nickname,fee,secret, secondSecret,callbackContext);
                }else {
                    // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    AppUtil.toastError(cordova.getContext(), "账户密码错误");

                }
            }
        });
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

    public void coreWithdraw(String dappID, String currency, long amount, String message, String secret, String secondSecret, CallbackContext callbackContext){
        Observable observable=createWithdrawObservable(dappID,currency,amount,message,secret,secondSecret);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AppUtil.toastError(cordova.getContext(), "提现失败");
                        //callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    }

                    @Override
                    public void onNext(AschResult aschResult) {
                        String rawJson = aschResult.getRawJson();
                        Log.i(TAG, "+++++++" + rawJson);
                        // callBack.onCallBack(rawJson);
                        // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, aschResult.getRawJson()));
                        AppUtil.toastSuccess(cordova.getContext(), "提现成功");
                        dismissDialog();

                    }
                });
    }

    public void coreTransfer(String dappID, String currency, String targetAddress, long amount, String message, long fee, String secret, String secondSecret, CallbackContext callbackContext){
        Observable observable=createTransferObservable(dappID, currency, targetAddress, amount, message, fee, secret, secondSecret);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AppUtil.toastError(cordova.getContext(), "内部转账失败");
                        //callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    }

                    @Override
                    public void onNext(AschResult aschResult) {
                        String rawJson = aschResult.getRawJson();
                        Log.i(TAG, "+++++++" + rawJson);
                        // callBack.onCallBack(rawJson);
                        // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, aschResult.getRawJson()));
                        AppUtil.toastSuccess(cordova.getContext(), "内部转账成功");
                        dismissDialog();

                    }
                });
    }

    public void coreSetNickname(String dappID, String nickname, long fee, String secret, String secondSecret, CallbackContext callbackContext){
        Observable observable=createSetNicknameObservable(dappID, nickname, fee, secret, secondSecret);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AppUtil.toastError(cordova.getContext(), "设置昵称失败");
                        //callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    }

                    @Override
                    public void onNext(AschResult aschResult) {
                        String rawJson = aschResult.getRawJson();
                        Log.i(TAG, "+++++++" + rawJson);
                        // callBack.onCallBack(rawJson);
                        // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, aschResult.getRawJson()));
                        AppUtil.toastSuccess(cordova.getContext(), "设置昵称成功");
                        dismissDialog();

                    }
                });
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


    private void showPasswordDialog(AllPasswdsDialog.OnConfirmationListenner confirmationListenner){
        boolean hasSecondSecret=getAccount().hasSecondSecret();
        dialog = new AllPasswdsDialog(this.cordova.getContext(),hasSecondSecret);
        dialog.setTitle(this.cordova.getContext().getString(R.string.account_input_title));
        dialog.show(confirmationListenner);
    }

    private void dismissDialog(){
      if (dialog!=null){
          dialog.dismiss();
          dialog=null;
      }
    }
}
