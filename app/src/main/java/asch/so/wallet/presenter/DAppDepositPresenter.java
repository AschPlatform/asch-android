package asch.so.wallet.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.contract.AssetTransferContract;
import asch.so.wallet.contract.DAppDepositContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.UIAAsset;
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

public class DAppDepositPresenter implements DAppDepositContract.Presenter {

    private static final String TAG=DAppDepositPresenter.class.getSimpleName();
    private DAppDepositContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;

    public DAppDepositPresenter(Context context, DAppDepositContract.View view) {
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
    public void transfer(String dappId, String currency, long amount, String message, String secret, String secondSecret, String password) {
        String encryptSecret=getAccount().getEncryptSeed();
         Subscription subscription= Observable.create(new Observable.OnSubscribe<AschResult>(){

                @Override
                public void call(Subscriber<? super AschResult> subscriber) {
                    String decryptSecret=Account.decryptSecret(password,encryptSecret);
                    if (!Validation.isValidSecret(decryptSecret)){
                        subscriber.onError(new Throwable("1"));
                        return;
                    }
                    AschResult result=AschSDK.Dapp.deposit(dappId,currency,amount,message,decryptSecret,secondSecret);
                    LogUtils.dTag(TAG,"transfer result:"+result==null?"null":result.getRawJson());
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
                            view.displayTransferResult(true,context.getString(R.string.deposit_success));
                        }
                    });
         subscriptions.add(subscription);
        }


    @Override
    public void loadAssets(String currency, boolean ignoreCache) {

        Subscription subscription=  Wallet.getInstance().loadAssets(ignoreCache, new Wallet.OnLoadAssetsListener() {
            @Override
            public void onLoadAllAssets(LinkedHashMap<String, BaseAsset> assetsMap, Throwable exception) {
                if (exception!=null){
                    view.displayError(new Throwable(context.getString(R.string.asset_get_error)));
                }else {
                    Iterator<Map.Entry<String,BaseAsset>> it=assetsMap.entrySet().iterator();
                    LinkedHashMap<String, BaseAsset> map=new LinkedHashMap<>();
                   // ArrayList<Delegate> delegates=new ArrayList<>();
                    while (it.hasNext()){
                        Map.Entry<String,BaseAsset> entry =it.next();
                       if (hasAsset(entry.getKey())){
                           map.put(entry.getKey(),entry.getValue());
                       }
                    }
                    view.displayAssets(map);
                }
            }
        });
        subscriptions.add(subscription);
    }

//    private int getSelectedIndex(List<UIAAsset> assets, String currency){
//        int index=0;
//        if (currency!=null && assets!=null){
//            for (int i = 0; i < assets.size(); i++) {
//                if (currency.equals(assets.get(i).getName()))
//                {
//                    index=i+1;
//                    break;
//                }
//            }
//        }
//        return index;
//    }

    private boolean hasAsset(String currency){
        return (getAccount()!=null && getAccount().getFullAccount()!=null && getAccount().getFullAccount().hasAsset(currency));
    }
}
