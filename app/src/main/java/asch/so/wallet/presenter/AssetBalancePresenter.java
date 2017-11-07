package asch.so.wallet.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import asch.so.base.view.UIException;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetBalanceContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/20.
 */

public class AssetBalancePresenter implements AssetBalanceContract.Presenter,Observer {
    private static  final  String TAG=AssetBalancePresenter.class.getSimpleName();

    private final  AssetBalanceContract.View view;
    private Context context;

    public AssetBalancePresenter(AssetBalanceContract.View view) {
        this.view = view;
        view.setPresenter(this);
        AccountsManager.getInstance().addObserver(this);
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
    public void loadAccount() {
        view.displayAccount(getAccount());
    }

    @Override
    public void loadAssets(){

        Account account = getAccount();
        String address=account.getAddress();
        ArrayList<Balance> list=new ArrayList<>();
        Observable  xasObservable = Observable.create(new Observable.OnSubscribe<List<Balance>>() {
            @Override
            public void call(Subscriber<? super List<Balance>> subscriber) {
                try {
                    AschResult result = AschSDK.Account.getBalance(address);
                    if (result!=null && result.isSuccessful()){
                        Log.i(TAG,result.getRawJson());
                        Map<String, Object> map =result.parseMap();
                        Balance xasBalance=new Balance();
                        xasBalance.setCurrency("XAS");
                        xasBalance.setBalance(String.valueOf(map.getOrDefault("balance","0")));
                        xasBalance.setPrecision(8);
                        list.add(xasBalance);
                        subscriber.onNext(list);
                        subscriber.onCompleted();
                    }else {
                        subscriber.onError(result.getException());
                    }
                }
                catch (Exception ex){
                    subscriber.onError(ex);
                }
            }
        });
        Observable  uiaOervable =
                Observable.create(new Observable.OnSubscribe<List<Balance>>(){
            @Override
            public void call(Subscriber<? super List<Balance>> subscriber) {
                AschResult result = AschSDK.UIA.getAddressBalances(address,100,0);
                Log.i(TAG,result.getRawJson());
                if (result.isSuccessful()){
                    JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                    JSONArray balanceJsonArray=resultJSONObj.getJSONArray("balances");
                    List<Balance> balances=JSON.parseArray(balanceJsonArray.toJSONString(),Balance.class);
                    list.addAll(balances);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(result.getException());
                }
            }
        });

        xasObservable.flatMap(new Func1<List<Balance>, Observable<List<Balance>>>() {
                    @Override
                    public Observable<List<Balance>> call(List<Balance> balances) {
                        return uiaOervable;
                    }
                })
                        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Balance>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("xasObservable error:",e.toString());
                        view.displayError(new UIException("获取余额错误"));
                    }

                    @Override
                    public void onNext(List<Balance> balances) {
                        if (balances!=null && balances.size()>0){
                            view.displayXASBalance(balances.get(0));
                        }
                        view.displayAssets(balances);
                    }
                });
//                .subscribe(new Action1<List<Balance>>() {
//                    @Override
//                    public void call(List<Balance> balances) {
//                        if (balances!=null && balances.size()>0){
//                            view.displayXASBalance(balances.get(0));
//                        }
//                        view.displayAssets(balances);
//                    }
//
//                });

    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @Override
    public void update(java.util.Observable observable, Object o) {
        if (observable instanceof AccountsManager){
            loadAccount();
            loadAssets();
        }
    }
}
