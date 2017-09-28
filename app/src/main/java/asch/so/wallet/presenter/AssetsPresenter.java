package asch.so.wallet.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

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

                ArrayList<Balance> list=new ArrayList<>();
                list.add(xasBalance);
//                list.add(new Balance());
//                list.add(new Balance());
//                list.add(new Balance());
//                list.add(new Balance());
//                list.add(new Balance());
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
