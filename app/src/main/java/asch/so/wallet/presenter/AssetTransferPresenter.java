package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.contract.AssetTransferContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.model.entity.UIAAsset;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
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
    public void transfer(String currency, String targetAddress, long amount, String message, String secret, String secondSecret, String password) {
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
                    Log.d(TAG,"transfer result:"+result==null?"null":result.getRawJson());
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
                                view.displayPasswordValidMessage(false,"用户密码不正确");
                            }else{
                                view.displayError(e);
                            }

//                                if ("2".equals(e.getMessage())){
//                                view.displayError(new Throwable("转账失败"));
//                            }else {
//                                view.displayError(new Throwable("转账失败"));
//                            }

                        }

                        @Override
                        public void onNext(AschResult aschResult) {
                            Log.i(TAG, "+++++++"+aschResult.getRawJson());
                            view.displayTransferResult(true,"转账成功");
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
                    view.displayError(new Throwable("获取资产错误"));
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
//        ArrayList<UIAAsset> list=new ArrayList<UIAAsset>();
//        Observable  uiaOervable =
//                Observable.create(new Observable.OnSubscribe<List<UIAAsset>>(){
//                    @Override
//                    public void call(Subscriber<? super List<UIAAsset>> subscriber) {
//                        AschResult result = AschSDK.UIA.getAssets(100,0);
//                        Log.i(TAG,result.getRawJson());
//                        if (result.isSuccessful()){
//                            JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
//                            JSONArray balanceJsonArray=resultJSONObj.getJSONArray("assets");
//                            List<UIAAsset> assets= JSON.parseArray(balanceJsonArray.toJSONString(),UIAAsset.class);
//                            list.addAll(assets);
//                            subscriber.onNext(list);
//                            subscriber.onCompleted();
//                        }else{
//                            subscriber.onError(result.getException());
//                        }
//                    }
//                });
//
//       Subscription subscription= uiaOervable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<List<UIAAsset>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(java.lang.Throwable e) {
//                        view.displayError(new Throwable("转账失败"));
//                    }
//
//                    @Override
//                    public void onNext(List<UIAAsset> assets) {
//                        ArrayList<UIAAsset> filterdAssets=new ArrayList<>();
//                        for (UIAAsset uiaAsset :
//                                assets) {
//                            if (hasAsset(uiaAsset.getName())){
//                                filterdAssets.add(uiaAsset);
//                            }
//                        }
//                        view.displayAssets(filterdAssets,getSelectedIndex(filterdAssets,currency));
//                    }
//                });
//       subscriptions.add(subscription);
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

    private boolean hasAsset(String currency){
        return (getAccount()!=null && getAccount().getFullAccount()!=null && getAccount().getFullAccount().hasAsset(currency));
    }
}
