package asch.io.wallet.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import asch.io.base.view.Throwable;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.DAppDepositContract;
import asch.io.wallet.model.entity.Account;
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

//        //TODO
//        Subscription subscription=  AssetManager.getInstance().loadAssets(new AssetManager.OnLoadAssetsListener() {
//            @Override
//            public void onLoadAllAssets(List<AschAsset> assetsMap, Throwable exception) {
//                if (exception!=null){
//                    view.displayError(new Throwable(context.getString(R.string.asset_get_error)));
//                }else {
////                    Iterator<Map.Entry<String,BaseAsset>> it=assetsMap.entrySet().iterator();
//                    LinkedHashMap<String, BaseAsset> map=new LinkedHashMap<>();
////                   // ArrayList<Delegate> delegates=new ArrayList<>();
////                    while (it.hasNext()){
////                        Map.Entry<String,BaseAsset> entry =it.next();
////                       if (hasAsset(entry.getKey())){
////                           map.put(entry.getKey(),entry.getValue());
////                       }
////                    }
//                    view.displayAssets(map);
//                }
//            }
//        });
//        subscriptions.add(subscription);
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


}
