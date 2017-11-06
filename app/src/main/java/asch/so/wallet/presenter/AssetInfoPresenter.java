package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetInfoContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.UIAAsset;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/11/5.
 */

public class AssetInfoPresenter implements AssetInfoContract.Presenter {

    private Context context;
    private AssetInfoContract.View view;

    public AssetInfoPresenter(Context context, AssetInfoContract.View view) {
        this.context=context;
        this.view = view;
        this.view.setPresenter(this);
    }

    private static  final  String TAG=AssetInfoContract.class.getSimpleName();
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
    public void loadAssets() {
        Account account = getAccount();
        String address=account.getAddress();
        ArrayList<UIAAsset> list=new ArrayList<UIAAsset>();
//        Observable xasObservable = Observable.create(new Observable.OnSubscribe<List<Balance>>() {
//            @Override
//            public void call(Subscriber<? super List<Balance>> subscriber) {
//                try {
//                    AschResult result = AschSDK.Account.getBalance(address);
//                    if (result!=null && result.isSuccessful()){
//                        Log.i(TAG,result.getRawJson());
//                        Map<String, Object> map =result.parseMap();
//                        Balance xasBalance=new Balance();
//                        xasBalance.setCurrency("XAS");
//                        xasBalance.setBalance(String.valueOf(map.getOrDefault("balance","0")));
//                        xasBalance.setPrecision(8);
//                        list.add(xasBalance);
//                        subscriber.onNext(list);
//                        subscriber.onCompleted();
//                    }else {
//                        subscriber.onError(result.getException());
//                    }
//                }
//                catch (Exception ex){
//                    subscriber.onError(ex);
//                }
//            }
//        });
        Observable  uiaOervable =
                Observable.create(new Observable.OnSubscribe<List<UIAAsset>>(){
                    @Override
                    public void call(Subscriber<? super List<UIAAsset>> subscriber) {
                        AschResult result = AschSDK.UIA.getAddressBalances(address,100,0);
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
//
//        xasObservable.flatMap(new Func1<List<Balance>, Observable<List<Balance>>>() {
//            @Override
//            public Observable<List<Balance>> call(List<Balance> balances) {
//                return uiaOervable;
//            }
//        })
        uiaOervable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<UIAAsset>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UIAAsset> assets) {
                        view.displayAssets(assets);
                    }
                });
//                .subscribe(new Action1<List<UIAAsset>>() {
//                    @Override
//                    public void call(List<UIAAsset> balances) {
//                        view.displayAssets(balances);
//                    }
//                });
    }
}
