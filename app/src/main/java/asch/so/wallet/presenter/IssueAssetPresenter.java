package asch.so.wallet.presenter;


import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import asch.so.wallet.P;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.IssueAssetContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.IssuerInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;


public class IssueAssetPresenter implements IssueAssetContract.Presenter {

    private CompositeSubscription subscriptions;
    Context context;
    IssueAssetContract.View view;
    private final static String TAG = IssueAssetPresenter.class.getSimpleName();

    public IssueAssetPresenter(Context context, IssueAssetContract.View view) {
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
        subscriptions.clear();
    }


    @Override
    public void issueAsset(String name, String amount, String password, String secondPassword) {



        String encryptSecret=AccountsManager.getInstance().getCurrentAccount().getEncryptSeed();
        String decryptSecret=Account.decryptSecret(password,encryptSecret);

        Observable uiaObservable =
                Observable.create(new Observable.OnSubscribe<IssuerInfo>(){
                    @Override
                    public void call(Subscriber<? super IssuerInfo> subscriber) {
                        AschResult result = AschSDK.UIA.issue(name,amount,decryptSecret,secondPassword);
                        LogUtils.iTag(TAG,result.getRawJson());
                        if (result.isSuccessful()){
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }else{
                            subscriber.onError(result.getException());
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
