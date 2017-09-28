package asch.so.wallet.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.wallet.TestData;
import asch.so.wallet.contract.AssetsContract;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
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
            }
        }).start();


        ArrayList<BaseAsset> list=new ArrayList<>();
        list.add(new BaseAsset());
        list.add(new BaseAsset());
        list.add(new BaseAsset());
        list.add(new BaseAsset());
        list.add(new BaseAsset());

        view.displayAssets(list);
    }


}
