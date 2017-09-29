package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import asch.so.base.presenter.BasePresenter;
import asch.so.wallet.AppConstants;
import asch.so.wallet.TestData;
import asch.so.wallet.contract.AssetTransferContract;
import asch.so.wallet.contract.AssetsContract;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/29.
 */

public class AssetTransferPresenter implements AssetTransferContract.Presenter {

    private static final String TAG=AssetTransferPresenter.class.getSimpleName();
    private AssetTransferContract.View view;
    private Context context;

    public AssetTransferPresenter(Context context, AssetTransferContract.View view) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }


    @Override
    public void transfer(String currency, String targetAddress, long amount, String message, String secret, String secondSecret) {

            Observable.create(new Observable.OnSubscribe<AschResult>(){

                @Override
                public void call(Subscriber<? super AschResult> subscriber) {
                    AschResult result=null;
                    if (AppConstants.XAS_NAME.equals(currency)){
                         result = AschSDK.Account.transfer(targetAddress,amount,message,secret,secondSecret);
                    }else {
                        result = AschSDK.UIA.transfer(currency,targetAddress,amount,message,secret,secondSecret);
                    }
                    if (result!=null && result.isSuccessful()){
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }else {
                        subscriber.onError(result!=null?result.getException():new Exception("result is null"));
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<AschResult>() {
                        @Override
                        public void call(AschResult aschResult) {
                            Log.i(TAG, "+++++++"+aschResult.getRawJson());
                            view.displayToast("转账成功");
                        }
                    });
        }
}
