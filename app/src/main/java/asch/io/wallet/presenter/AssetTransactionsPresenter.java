package asch.io.wallet.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import asch.io.base.adapter.page.IPage;
import asch.io.base.view.Throwable;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.AssetTransactionsContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.GatewayAccount;
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
 * Created by kimziv on 2017/10/13.
 */

public class AssetTransactionsPresenter implements AssetTransactionsContract.Presenter {
    private final static String TAG = AssetTransactionsPresenter.class.getSimpleName();
    private AssetTransactionsContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;
    IPage pager;

    public AssetTransactionsPresenter(Context ctx, AssetTransactionsContract.View view) {
        this.view = view;
        this.context = ctx;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);
    }

    @Override
    public void subscribe() { }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadGatewayAddress(String gateway,String address) {

        Subscription subscription= Observable.create(new Observable.OnSubscribe<GatewayAccount>(){
            @Override
            public void call(Subscriber<? super GatewayAccount> subscriber) {

                AschResult result = AschSDK.Gateway.getGatewayAccount(gateway,address);
                LogUtils.dTag(TAG,"getGatewayAccount result:"+result==null?"null":result.getRawJson());
                JSONObject object = JSONObject.parseObject(result.getRawJson()).getJSONObject("account");
                GatewayAccount account  = new GatewayAccount();
                if (object!=null)
                   account = object.toJavaObject(GatewayAccount.class);

                if (result!=null && result.isSuccessful()){
                    subscriber.onNext(account);
                    subscriber.onCompleted();
                }else {
                    subscriber.onError(new Throwable(result.getError()));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GatewayAccount>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(e);
                    }

                    @Override
                    public void onNext(GatewayAccount aschResult) {
                        if (!TextUtils.isEmpty(aschResult.getOutAddress()))
                            view.showDeposit(aschResult.getOutAddress());
                        else
                            view.showCreateAccountDialog();
                    }
                });
        subscriptions.add(subscription);
    }


    @Override
    public void createGatewayAccount(String gateway, String message, String secret, String secondSecret) {
        String encryptSecret=AccountsManager.getInstance().getCurrentAccount().getEncryptSeed();
        Subscription subscription= Observable.create(new Observable.OnSubscribe<AschResult>(){
            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                String decryptSecret=Account.decryptSecret(secret,encryptSecret);
                if (!Validation.isValidSecret(decryptSecret)){
                    subscriber.onError(new Throwable("1"));
                    return;
                }
                AschResult result = AschSDK.Gateway.openGatewayAccount(gateway,message,decryptSecret,secondSecret);
                LogUtils.dTag(TAG,"getGatewayAccount result:"+result==null?"null":result.getRawJson());
                if (result!=null && result.isSuccessful()){
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }else {
                    subscriber.onError(new Throwable(result.getError()));
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
                        view.displayError(e);
                    }

                    @Override
                    public void onNext(AschResult aschResult) {
                        view.showCreateSuccessDialog();
                    }
                });
        subscriptions.add(subscription);
    }
}
