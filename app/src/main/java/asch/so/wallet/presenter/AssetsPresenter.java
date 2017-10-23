package asch.so.wallet.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import asch.so.base.presenter.BasePresenter;
import asch.so.wallet.TestData;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetsContract;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/20.
 */

public class AssetsPresenter implements AssetsContract.Presenter {
    private static  final  String TAG=AssetsPresenter.class.getSimpleName();

    private final  AssetsContract.View view;
    private Context context;

    public AssetsPresenter(AssetsContract.View view) {
        this.view = view;
        view.setPresenter(this);
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
//        Account account=new Account();
//        account.setName("牛牛");
//        account.setAddress(TestData.address);
        view.displayAccount(getAccount());
    }

    @Override
    public void loadAssets() {

        Account account = getAccount();
        String address=account.getAddress();
        ArrayList<Balance> list=new ArrayList<>();
        Observable  xasObservable = Observable.create(new Observable.OnSubscribe<List<Balance>>() {
            @Override
            public void call(Subscriber<? super List<Balance>> subscriber) {
                //Account account = getAccount();
                AschResult result = AschSDK.Account.getBalance(address);
                Log.i(TAG,result.getRawJson());
                if (result.isSuccessful()){
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
                .subscribe(new Action1<List<Balance>>() {
                    @Override
                    public void call(List<Balance> balances) {
                        if (balances!=null && balances.size()>0){
                            view.displayXASBalance(balances.get(0));
                        }
                        view.displayAssets(balances);
                    }
                });

    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


}
