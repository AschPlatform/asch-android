package asch.io.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.AssetInfoContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.UIAAsset;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/11/5.
 */

public class AssetInfoPresenter implements AssetInfoContract.Presenter {

    private Context context;
    private AssetInfoContract.View view;
    private CompositeSubscription subscriptions;
    public AssetInfoPresenter(Context context, AssetInfoContract.View view) {
        this.context=context;
        this.view = view;
        this.view.setPresenter(this);
        this.subscriptions=new CompositeSubscription();
    }

    private static  final  String TAG=AssetInfoContract.class.getSimpleName();
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
    public void loadAssets() {
        Account account = getAccount();
        String address=account.getAddress();
        ArrayList<UIAAsset> list=new ArrayList<UIAAsset>();
        Observable  uiaOervable =
                Observable.create(new Observable.OnSubscribe<List<UIAAsset>>(){
                    @Override
                    public void call(Subscriber<? super List<UIAAsset>> subscriber) {
                        AschResult result = AschSDK.Account.getBalanceV2(address,100,0);
                        LogUtils.iTag(TAG,result.getRawJson());
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
      Subscription subscription = uiaOervable.subscribeOn(Schedulers.io())
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
      subscriptions.add(subscription);
    }
}
