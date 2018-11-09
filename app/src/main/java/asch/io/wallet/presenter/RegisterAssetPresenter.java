package asch.io.wallet.presenter;


import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import asch.io.base.view.Throwable;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.RegisterAssetContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.IssuerInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;


public class RegisterAssetPresenter implements RegisterAssetContract.Presenter {
    private final static String TAG = RegisterAssetPresenter.class.getSimpleName();

    Context context;
    RegisterAssetContract.View view;
    private CompositeSubscription subscriptions;

    public RegisterAssetPresenter(Context context, RegisterAssetContract.View view) {
        this.context=context;
        this.view=view;
        this.view.setPresenter(this);
        this.subscriptions=new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        this.subscriptions=new CompositeSubscription();
    }


    @Override
    public void register(String currency, String desc, String maximum, String precision, String secret, String secondSecret) {
        String encryptSecret=AccountsManager.getInstance().getCurrentAccount().getEncryptSeed();
        String decryptSecret=Account.decryptSecret(secret,encryptSecret);
        Observable uiaObservable =
                Observable.create(new Observable.OnSubscribe<IssuerInfo>(){
                    @Override
                    public void call(Subscriber<? super IssuerInfo> subscriber) {
                        AschResult result = AschSDK.UIA.createAsset(currency,desc,maximum,precision,decryptSecret,secondSecret);
                        LogUtils.iTag(TAG,result.getRawJson());
                        if (result.isSuccessful()){
                            String transactionId = JSONObject.parseObject(result.getRawJson()).getString("transactionId");
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }else{
                            subscriber.onError(new Throwable(result.getError()));
                        }
                    }
                });
        Subscription subscription = uiaObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<IssuerInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable throwable) {
                        view.displayError(throwable);
                    }

                    @Override
                    public void onNext(IssuerInfo assets) {
                        view.displaySuccess();
                    }
                });
        subscriptions.add(subscription);
    }
}
