package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import asch.so.base.view.UIException;
import asch.so.wallet.AppConstants;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetTransferContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.UIAAsset;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

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
    public void transfer(String currency, String targetAddress, long amount, String message, String secret, String secondSecret) {

         Subscription subscription= Observable.create(new Observable.OnSubscribe<AschResult>(){

                @Override
                public void call(Subscriber<? super AschResult> subscriber) {
                    AschResult result=null;
                    if (AppConstants.XAS_NAME.equals(currency)){
                         result = AschSDK.Account.transfer(targetAddress,amount,message,secret,secondSecret);
                    }else {
                        result = AschSDK.UIA.transfer(currency,targetAddress,amount,message,secret,secondSecret);
                    }
                    Log.d(TAG,"transfer result:"+result==null?"null":result.getRawJson());
                    if (result!=null && result.isSuccessful()){
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }else {
                        subscriber.onError(new UIException("转账失败"));
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
                        public void onError(Throwable e) {
                            view.displayError(new UIException("转账失败"));
                        }

                        @Override
                        public void onNext(AschResult aschResult) {
                            Log.i(TAG, "+++++++"+aschResult.getRawJson());
                            view.displayToast("转账成功");
                        }
                    });
         subscriptions.add(subscription);
        }

    @Override
    public void loadAssets(String currency) {
       // Account account = getAccount();
        //String address=account.getAddress();
        ArrayList<UIAAsset> list=new ArrayList<UIAAsset>();
        Observable  uiaOervable =
                Observable.create(new Observable.OnSubscribe<List<UIAAsset>>(){
                    @Override
                    public void call(Subscriber<? super List<UIAAsset>> subscriber) {
                        AschResult result = AschSDK.UIA.getAssets(100,0);
                        Log.i(TAG,result.getRawJson());
                        if (result.isSuccessful()){
                            JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                            JSONArray balanceJsonArray=resultJSONObj.getJSONArray("assets");
                            List<UIAAsset> assets= JSON.parseArray(balanceJsonArray.toJSONString(),UIAAsset.class);
                            list.addAll(assets);
                            subscriber.onNext(list);
                            subscriber.onCompleted();
                        }else{
                            subscriber.onError(result.getException());
                        }
                    }
                });

       Subscription subscription= uiaOervable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<UIAAsset>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.displayError(new UIException("转账失败"));
                    }

                    @Override
                    public void onNext(List<UIAAsset> assets) {
                        ArrayList<UIAAsset> filterdAssets=new ArrayList<>();
                        for (UIAAsset uiaAsset :
                                assets) {
                            if (hasAsset(uiaAsset.getName())){
                                filterdAssets.add(uiaAsset);
                            }
                        }
                        view.displayAssets(filterdAssets,getSelectedIndex(filterdAssets,currency));
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

    private boolean hasAsset(String currency){
        return (getAccount()!=null && getAccount().getFullAccount()!=null && getAccount().getFullAccount().hasAsset(currency));
    }
}
