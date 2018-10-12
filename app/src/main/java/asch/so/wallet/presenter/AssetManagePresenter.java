package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import asch.so.wallet.AppConfig;
import asch.so.wallet.AppConstants;
import asch.so.wallet.ApplicationModule;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.AssetManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.contract.AccountsContract;
import asch.so.wallet.contract.AssetManageContract;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.AschAsset;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.FullAccount;
import asch.so.wallet.presenter.component.DaggerPresenterComponent;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AssetManagePresenter implements AssetManageContract.Presenter{
    private final String TAG = AssetManagePresenter.class.getSimpleName();
    private AssetManageContract.View view;

    private CompositeSubscription subscriptions;

    @Inject
    AccountsDao accountsDao;

    List<Balance> allList;


    @Inject
    public AssetManagePresenter(Context context, AssetManageContract.View view){

        this.view=view;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);


//        DaggerPresenterComponent.builder()
//                .applicationModule(new ApplicationModule(context))
//                .build().inject(this);
    }

    @Override
    public void subscribe() {
        loadAllAssets();
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }


    public void loadAllAssets() {

        HashMap<String, BaseAsset> assetHashMap = new HashMap<String, BaseAsset>();
//        assetHashMap = AssetManager.getInstance().;

        List<AschAsset> uiaList = new ArrayList<>();
        List<AschAsset> gatewayList = new ArrayList<>();
        uiaList = AssetManager.getInstance().queryUiaAssets();
        gatewayList = AssetManager.getInstance().queryGatewayAssets();

        view.displayGatewayAssets(gatewayList);
        view.displayUiaAssets(uiaList);

    }






    @Override
    public void saveCurrentAssets(String address) {

    }
}
