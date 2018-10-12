package asch.so.wallet.accounts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;

import asch.so.base.view.Throwable;
import asch.so.wallet.model.entity.AschAsset;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.GatewayAsset;
import asch.so.wallet.model.entity.UIAAsset;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.Gateway;

public class AssetManager extends Observable {

    //读取所有的，保存到本地。

    private static final String TAG = AssetManager.class.getSimpleName();
    private static AssetManager assetManager = null;

    public static AssetManager getInstance(){
        if (assetManager == null)
            assetManager = new AssetManager();
        return assetManager;

    }

    private Realm getRealm(){
        //TODO 地址读取、排序、刷新
//        String address = AccountsManager.getInstance().getCurrentAccount().getAddress();
        RealmConfiguration config = new RealmConfiguration.Builder().name("test1"+".realm").build();
        return Realm.getInstance(config);
    }



    public void addAssets(List<AschAsset> assets){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(assets);
            }
        });
    }


    public void addAsset(AschAsset asset){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(asset);
            }
        });
    }

    public  List<AschAsset> getBalances() {
        return queryBalances();
    }


    public AschAsset queryAschAssetByName(String name){
        AschAsset results = getRealm().where(AschAsset.class).equalTo("name",name).findFirst();
        return results;
    }


    public RealmResults<AschAsset> queryBalances(){
        RealmResults<AschAsset> results = getRealm().where(AschAsset.class).greaterThan("trueBalance",(float)0).findAll();
        return results;
    }

    public RealmResults<AschAsset> queryAllAssets(){
        RealmResults<AschAsset> results = getRealm().where(AschAsset.class).findAll();
        return results;
    }

    public RealmResults<AschAsset> queryAssetsForShow(){

        RealmResults<AschAsset> results = getRealm().where(AschAsset.class)
                .equalTo("showState",AschAsset.STATE_SHOW)
//                .or()
//                .greaterThan("trueBalance",(float) 0)
//                .equalTo("showState",AschAsset.STATE_UNSETTING)
                .findAll();

        return results;
    }

    public RealmResults<AschAsset> queryUiaAssets(){
        RealmResults<AschAsset> results = getRealm().where(AschAsset.class).equalTo("type",AschAsset.TYPE_UIA).findAll();
        return results;
    }

    public RealmResults<AschAsset> queryGatewayAssets(){
        RealmResults<AschAsset> results = getRealm().where(AschAsset.class).equalTo("type",AschAsset.TYPE_GATEWAY).findAll();
        return results;
    }


    public void updateAssetShowState(AschAsset aschAsset,int state){

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                aschAsset.setShowState(state);
               // realm.insertOrUpdate(aschAsset);
            }
        });
    }



    public  interface OnLoadAssetsListener{
        void onLoadAllAssets(List<AschAsset> assetsMap, Throwable exception);
    }




    //得到所有的资产与余额
    public Subscription loadBalanceAndAssets(OnLoadAssetsListener callback,String address) {
        rx.Observable balanceAndAssetsObservable = balanceAndAssetsObservable(address);
        Subscription subscription= balanceAndAssetsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<AschAsset>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        // view.displayError(new Throwable("转账失败"));
                        if (callback!=null){
//                            callback.onLoadAllAssets(allAssetsLocal,new Throwable(e!=null?e.getMessage():"get all Assets error"));
                        }
                    }

                    @Override
                    public void onNext(List<AschAsset> assets) {

//                        addAssets(assets);

                        if (callback!=null){
                            callback.onLoadAllAssets(assets,null);
                        }
                    }
                });
        return subscription;
        //subscriptions.add(subscription);
    }

    private void initAllAssets(List<UIAAsset> uia,List<GatewayAsset> gateway){

        for (UIAAsset u:uia){
            AschAsset asch = new AschAsset();
            asch = u.toAschAsset();
            if(!hasAsset(asch)){
                addAsset(asch);
            }
        }
        for (GatewayAsset g:gateway){
            AschAsset asch = new AschAsset();
            asch = g.toAschAsset();
            if(!hasAsset(asch)){
                addAsset(asch);
            }
        }
    }

    private void setBalanceToRealm(List<Balance> balances){

        LinkedHashMap<String,Balance> balanceHashMap = new LinkedHashMap<>();

        for (Balance balance : balances) {

            String name = "";
            //UIA余额
            if (balance.getFlag()==2){
                Balance.UIAAsset uiaAsset=JSON.parseObject(balance.getAssetJson(), Balance.UIAAsset.class);
                balance.setUiaAsset(uiaAsset);
                balance.setType(BaseAsset.TYPE_UIA);
                name = uiaAsset.getName();
            }
            //Gateway余额
            else if (balance.getFlag()==3){
                Balance.GatewayAsset gatewayAsset=JSON.parseObject(balance.getAssetJson(), Balance.GatewayAsset.class);
                balance.setGatewayAsset(gatewayAsset);
                balance.setType(BaseAsset.TYPE_GATEWAY);
                name = gatewayAsset.getSymbol();
            }

            balanceHashMap.put(name,balance);

        }


        RealmResults<AschAsset> results = getRealm().where(AschAsset.class).findAll();
        getRealm().beginTransaction();
        for (AschAsset aschAsset:results){
            if (balanceHashMap.containsKey(aschAsset.getName())){
                int savedState = aschAsset.getShowState();
                Balance balance = balanceHashMap.get(aschAsset.getName());
                aschAsset.setBalance(balance.getBalance());
                aschAsset.setTrueBalance(balance.getRealBalance());
                if (savedState!=AschAsset.STATE_UNSHOW)
                    aschAsset.setShowState(AschAsset.STATE_SHOW);
            }
        }

        getRealm().commitTransaction();


    }

    //操作余额与全部资产
    public rx.Observable<List<AschAsset>> balanceAndAssetsObservable(String address){
        rx.Observable allAssetObservable = allAssetsObservable();
        rx.Observable balanceObservable = balanceObservable(address);
        rx.Observable combinedObservable = rx.Observable.combineLatest(allAssetObservable, balanceObservable, new Func2<List<Object>, List<Balance>, List<AschAsset>>() {
            @Override
            public List<AschAsset> call(List<Object> aschAssets, List<Balance> balances) {

                setBalanceToRealm(balances);

                return null;
            }
        });
        return  combinedObservable;
    }


    //读取写入全部资产
    public rx.Observable<List<Object>> allAssetsObservable(){
        rx.Observable uiaAssetObv = uiaObservable();
        rx.Observable gatewayObv = gateWayObservable();
        rx.Observable combinedObservable = rx.Observable.combineLatest(uiaAssetObv, gatewayObv, new Func2<List<UIAAsset>, List<GatewayAsset>, List<Object>>() {
            @Override
            public List<Object> call(List<UIAAsset> uiaAssets, List<GatewayAsset> gatewayAssets) {

                initAllAssets(uiaAssets,gatewayAssets);

                return null;
            }
        });
        return  combinedObservable;
    }

    public  rx.Observable<List<UIAAsset>> uiaObservable(){
        return rx.Observable.create(new rx.Observable.OnSubscribe<List<UIAAsset>>(){
            @Override
            public void call(Subscriber<? super List<UIAAsset>> subscriber) {
                AschResult resultUIA = AschSDK.UIA.getAssets(100,0);
                LogUtils.iTag(TAG,resultUIA.getRawJson());
                if (resultUIA.isSuccessful()){
                    //解析UIA资产
                    String rawJson=resultUIA.getRawJson();
                    JSONObject resultJSONObj=JSONObject.parseObject(rawJson);
                    JSONArray assetJsonArray=resultJSONObj.getJSONArray("assets");
                    List<UIAAsset> assetsUIA= JSON.parseArray(assetJsonArray.toJSONString(),UIAAsset.class);
                    subscriber.onNext(assetsUIA);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(resultUIA.getException());
                }
            }
        });
    }

    public  rx.Observable<List<GatewayAsset>> gateWayObservable(){
        return rx.Observable.create(new rx.Observable.OnSubscribe<List<GatewayAsset>>(){
            @Override
            public void call(Subscriber<? super List<GatewayAsset>> subscriber) {
                AschResult resultGateway = AschSDK.Gateway.getGatewayAssets(100,0);
                LogUtils.iTag(TAG,resultGateway.getRawJson());
                if (resultGateway.isSuccessful()){
                    //解析Gateway资产
                    String rawJsonGateway=resultGateway.getRawJson();
                    JSONObject resultJSONObjGateway=JSONObject.parseObject(rawJsonGateway);
                    JSONArray assetJsonArrayGateway=resultJSONObjGateway.getJSONArray("currencies");
                    List<GatewayAsset> assetsGateway= JSON.parseArray(assetJsonArrayGateway.toJSONString(),GatewayAsset.class);
                    subscriber.onNext(assetsGateway);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(resultGateway.getException());
                }
            }
        });
    }

    //读取余额
    public static   rx.Observable<List<Balance>> balanceObservable(String address){
        return rx.Observable.create(new rx.Observable.OnSubscribe<List<Balance>>(){
            @Override
            public void call(Subscriber<? super List<Balance>> subscriber) {
                AschResult result = AschSDK.Account.getBalanceV2(address, 100, 0);
                LogUtils.iTag(TAG, result.getRawJson());
                if (result.isSuccessful()) {
                    JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                    JSONArray balanceJsonArray = resultJSONObj.getJSONArray("balances");
                    List<Balance> balances = JSON.parseArray(balanceJsonArray.toJSONString(), Balance.class);

                    subscriber.onNext(balances);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                    // subscriber.onError(result.getException());
                }
            }
        });
    }


    public Boolean hasAsset(AschAsset aschAsset){
        if (getRealm().where(AschAsset.class).equalTo("aid",aschAsset.getAid()).findAll().size()>0)
            return true;
        else
            return false;
    }


}
