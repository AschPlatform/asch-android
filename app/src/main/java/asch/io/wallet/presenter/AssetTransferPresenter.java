package asch.io.wallet.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import asch.io.base.view.Throwable;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.AssetTransferContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.UIAAsset;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/9/29.
 */

public class AssetTransferPresenter implements AssetTransferContract.Presenter {

    private static final String TAG=AssetTransferPresenter.class.getSimpleName();
    private AssetTransferContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;

    public AssetTransferPresenter(Context context, AssetTransferContract.View view) {
        this.view = view;
        this.context = context;
        this.subscriptions=new CompositeSubscription();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        this.subscriptions.clear();
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void transfer(String currency, String targetAddress, String amount, String message, String secret, String secondSecret, String password) {
        String encryptSecret=getAccount().getEncryptSeed();
         Subscription subscription= Observable.create(new Observable.OnSubscribe<AschResult>(){

                @Override
                public void call(Subscriber<? super AschResult> subscriber) {
                    String decryptSecret=Account.decryptSecret(password,encryptSecret);
                    if (!Validation.isValidSecret(decryptSecret)){
                        subscriber.onError(new Throwable("1"));
                        return;
                    }
                    AschResult result=null;
                    if (AppConstants.XAS_NAME.equals(currency)){
                         result = AschSDK.Account.transfer(targetAddress,amount,message,decryptSecret,secondSecret);
                    }else {
                        result = AschSDK.UIA.transfer(currency,targetAddress,amount,message,decryptSecret,secondSecret);
                    }
                    LogUtils.dTag(TAG,"transfer result:"+result==null?"null":result.getRawJson());
                    if (result!=null && result.isSuccessful()){
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }else {
                        subscriber.onError(new Throwable(result.getError()));
//                        subscriber.onError(new Throwable("2"));
                       // subscriber.onError(result!=null?result.getException():new Exception("result is null"));
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
                        public void onError(java.lang.Throwable e) {
                            if ("1".equals(e.getMessage()))
                            {
                                view.displayPasswordValidMessage(false,context.getString(R.string.account_password_error));
                            }else{
                                view.displayError(e);
                            }

                        }

                        @Override
                        public void onNext(AschResult aschResult) {
                            LogUtils.iTag(TAG, "+++++++"+aschResult.getRawJson());
                            view.displayTransferResult(true,context.getString(R.string.transfer_success));
                        }
                    });
         subscriptions.add(subscription);
        }




    private int getSelectedIndex(List<UIAAsset> assets, String currency){
        int index=0;
        if (currency!=null && assets!=null){
            for (int i = 0; i < assets.size(); i++) {
                if (currency.equals(assets.get(i).getName()))
                {
                    index=i+1;
                    break;
                }
            }
        }
        return index;
    }


}
