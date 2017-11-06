package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.view.UIException;
import asch.so.wallet.AppConstants;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetTransferContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.UIAAsset;
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

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
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
//                    .subscribe(new Action1<AschResult>() {
//                        @Override
//                        public void call(AschResult aschResult) {
//                            Log.i(TAG, "+++++++"+aschResult.getRawJson());
//                            view.displayToast("转账成功");
//                        }
//                    });
        }

    @Override
    public void loadAssets() {
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

        uiaOervable.subscribeOn(Schedulers.io())
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
                        view.displayAssets(assets);
                    }
                });
//                .subscribe(new Action1<List<UIAAsset>>() {
//                    @Override
//                    public void call(List<UIAAsset> assets) {
////                        if (balances!=null && balances.size()>0){
////                            view.displayXASBalance(balances.get(0));
////                        }
//                        view.displayAssets(assets);
//                    }
//                });
    }
}
