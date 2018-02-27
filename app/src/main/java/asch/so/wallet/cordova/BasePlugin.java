package asch.so.wallet.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.widget.DAppPasswordDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2018/2/27.
 */

public class BasePlugin extends CordovaPlugin {

    private static final String TAG=BasePlugin.class.getSimpleName();
    protected DAppPasswordDialog dialog;

    protected Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }


    protected void showPasswordDialog(DAppPasswordDialog.OnConfirmationListenner confirmationListenner){
        boolean hasSecondSecret=getAccount().hasSecondSecret();
        dialog = new DAppPasswordDialog(this.cordova.getContext());
        dialog.setTitle(this.cordova.getContext().getString(R.string.account_input_title));
        dialog.show(confirmationListenner);
    }

    protected void dismissDialog(){
        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }

    protected void invokeContract(String dappID, int type, long fee, String[] args, String secret, CallbackContext callbackContext){
        Observable.create(new Observable.OnSubscribe<AschResult>() {

            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                AschResult result = AschSDK.Dapp.invokeContract(dappID, type, fee,args, secret);
                if (result != null && result.isSuccessful()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(result!=null?result.getError():"result is null"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AppUtil.toastError(cordova.getContext(), e.getMessage());
                        dismissDialog();
                        callbackContext.error(e.getMessage());
                    }

                    @Override
                    public void onNext(AschResult result) {

                        AppUtil.toastSuccess(cordova.getContext(), getString(R.string.opreation_success));
                        dismissDialog();
                        callbackContext.success();
                    }
                });
    }

    protected String getString(int resId){
        return  cordova.getContext().getString(resId);
    }

}
