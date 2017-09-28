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
import asch.so.wallet.contract.AssetsContract;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import rx.Observable;
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

    @Override
    public void loadAssets() {

        ArrayList<Balance> list=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Account account = AccountsDao.getInstance().queryCurrentAccount();
                AschResult result = AschSDK.Account.getBalance(TestData.address);
                Log.i(TAG,result.getRawJson());
                Map<String, Object> map =result.parseMap();
                Balance xasBalance=new Balance();
                xasBalance.setCurrency("XAS");
                xasBalance.setBalance(map.getOrDefault("balance","0")+"");
                xasBalance.setPrecision(8);

                result = AschSDK.UIA.getAddressBalances(TestData.address,100,0);

                list.add(xasBalance);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.displayAssets(list);
                    }
                });

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                AschResult result = AschSDK.UIA.getAddressBalances(TestData.address,100,0);
                Log.i(TAG,result.getRawJson());
                JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                JSONArray balanceJsonArray=resultJSONObj.getJSONArray("balances");
                List<Balance> balances=JSON.parseArray(balanceJsonArray.toJSONString(),Balance.class);
                list.addAll(balances);
//                for (Object balanceJsonObj :
//                        balanceJsonArray) {
//                    Balance balance=((JSONObject)balanceJsonObj).toJavaObject(Balance.class);
//                    list.add(balance);
//                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.displayAssets(list);
                    }
                });

            }
        }).start();
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


}
