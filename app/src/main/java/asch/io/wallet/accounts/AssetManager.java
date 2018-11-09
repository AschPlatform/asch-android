package asch.io.wallet.accounts;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;

import asch.io.base.view.Throwable;
import asch.io.wallet.AppConstants;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.model.entity.Balance;
import asch.io.wallet.model.entity.BaseAsset;
import asch.io.wallet.model.entity.FullAccount;
import asch.io.wallet.model.entity.GatewayAsset;
import asch.io.wallet.model.entity.UIAAsset;
import asch.io.wallet.model.entity.UserAsset;
import asch.io.wallet.util.AppUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

public class AssetManager extends Observable {

    private static final String TAG = AssetManager.class.getSimpleName();
    private static AssetManager assetManager = null;
    LinkedHashMap<String,AschAsset> assetHashMap = new LinkedHashMap<>();
    public static AssetManager getInstance(){
        if (assetManager == null)
            assetManager = new AssetManager();
        return assetManager;

    }

    public AssetManager(){
       loadAccountAssets();
    }



    public void loadAccountAssets(){
        Account account = AccountsManager.getInstance().getCurrentAccount();
        String ads = account.getAddress();
        String xasBalance = account.getFullAccount().getAccount().getBalance();
        long locked = account.getFullAccount().getAccount().getLockedAmount();
        long longBalance = Long.parseLong(xasBalance);
        long longTotal = locked+longBalance;
        String xasTotal = AppUtil.getTotalBalanceString(longTotal,AppConstants.PRECISION);
        AschAsset aschAsset = new AschAsset();
        aschAsset.setBalance(xasBalance);
        aschAsset.setXasTotal(xasTotal);
        aschAsset.setName(AppConstants.XAS_NAME);
        aschAsset.setType(AschAsset.TYPE_XAS);
        aschAsset.setShowState(AschAsset.STATE_SHOW);
        aschAsset.setPrecision(AppConstants.PRECISION);
        aschAsset.setTrueBalance ((float) (Double.parseDouble(aschAsset.getBalance())/(Math.pow(10,AppConstants.PRECISION))));
        addAsset(aschAsset);

//        RealmResults<AschAsset> results = getRealm().where(AschAsset.class).findAll();
//        for(AschAsset localAsset:results){
//            assetHashMap.put(localAsset.getName(),localAsset);
//        }

//        loadBalanceAndAssets(new OnLoadAssetsListener() {
//            @Override
//            public void onLoadAllAssets(LinkedHashMap<String,AschAsset> assetsMap, Throwable exception) {
////                    addAssets(assets);
//
//            }
//        },ads);
    }

    public void saveBalanceAndAsset(LinkedHashMap<String,AschAsset>assetsMap){
        for (AschAsset asset:assetsMap.values()){
            if (hasAsset(asset)){
                int state = queryAssetStateByName(asset.getName());
                if (state!=AschAsset.STATE_UNSHOW&&asset.getTrueBalance()>0)
                    state=AschAsset.STATE_SHOW;
                asset.setShowState(state);
            }else {
                if (asset.getTrueBalance()>0)
                    asset.setShowState(AschAsset.STATE_SHOW);
                else
                    asset.setShowState(AschAsset.STATE_UNSETTING);
            }
            addAsset(asset);
        }
    }

    private Realm getRealm(){
        //TODO 排序
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(AccountsManager.getInstance().getCurrentAccount().getAddress()+".realm").build();

        Realm instance = Realm.getInstance(config);
        return instance;
    }


    public void delAccountAsset(Account account){

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(account.getAddress()+".realm").build();
        Realm instance = Realm.getInstance(config);
        instance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                instance.delete(AschAsset.class);

            }
        });

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

    public int queryAssetStateByName(String name){
        int state = getRealm().where(AschAsset.class).equalTo("name",name).findFirst().getShowState();
        return state;
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
        if (queryAllAssets().size()<1)
            loadAccountAssets();

        RealmResults<AschAsset> results = getRealm().where(AschAsset.class)
                .equalTo("showState",AschAsset.STATE_SHOW)
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
        void onLoadAllAssets(LinkedHashMap<String,AschAsset> assets, Throwable exception);
    }


//    public Subscription  xasBalanceSubscription() {
//        rx.Observable<FullAccount>  observable = AccountsManager.getInstance().loadAccountAndAssetsObservable();
//        Subscription subscription = observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<FullAccount>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(java.lang.Throwable e) {
//                        LogUtils.dTag("xasObservable error:",e.toString());
//                    }
//
//                    @Override
//                    public void onNext(FullAccount fullAccount) {
//                        LogUtils.dTag(TAG,"FullAccount info:"+fullAccount.getAccount().getAddress()+" balances:"+fullAccount.getAccount().getBalance().toString());
//                        AccountsManager.getInstance().getCurrentAccount().setFullAccount(fullAccount);
////                        AssetManager.getInstance().loadAccountAssets();
////                        view.displayAssets(AssetManager.getInstance().queryAssetsForShow());
//                    }
//                });
//        return subscription;
//    }



    //得到所有的资产与余额
    public Subscription loadBalanceAndAssets(OnLoadAssetsListener callback,String address) {
        rx.Observable balanceAndAssetsObservable = balanceAndAssetsObservable(address);
        Subscription subscription= balanceAndAssetsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LinkedHashMap<String,AschAsset>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        if (callback!=null){
//                            callback.onLoadAllAssets(allAssetsLocal,new Throwable(e!=null?e.getMessage():"get all Assets error"));
                        }
                    }

                    @Override
                    public void onNext(LinkedHashMap<String,AschAsset> assets) {

//                        addAssets(assets);

                        if (callback!=null){
                            callback.onLoadAllAssets(assets,null);
                        }
                    }
                });
        return subscription;
        //subscriptions.add(subscription);
    }

    private List<UserAsset> initAllAssets(List<UIAAsset> uia,List<GatewayAsset> gateway){

        List<UserAsset> userAssetList = new ArrayList<>();
        for (UIAAsset u:uia){
            UserAsset asset = new UserAsset();
            asset = u.toUserAsset();
            userAssetList.add(asset);
//            if(!hasAsset(asset)){
//                addAsset(asset);
//            }
        }
        for (GatewayAsset g:gateway){
            UserAsset asch = new UserAsset();
            asch = g.toAschAsset();
            userAssetList.add(asch);
//            if(!hasAsset(asch)){
//                addAsset(asch);
//            }
        }
        return userAssetList;
    }

    private LinkedHashMap<String,AschAsset> setAssetToRealm(List<UserAsset> assetList,List<Balance> balances){
        LinkedHashMap<String,AschAsset> aschAssetMap = new LinkedHashMap<>();

        //生成资产币种map
        for(UserAsset asset:assetList){
            String name = "";
            AschAsset asch = new AschAsset();
            if (asset.getType()==UserAsset.TYPE_UIA){
                name = asset.getName();
                asch.setName(asset.getName());
                asch.setTid(asset.getTid());
                asch.setType(AschAsset.TYPE_UIA);
                asch.setMaximum(asset.getMaximum());
                asch.setPrecision(asset.getPrecision());
                asch.setQuantity(asset.getQuantity());
                asch.setDesc(asset.getDesc());
                asch.setIssuerId(asset.getIssuerId());
                asch.setTimestamp(asset.getTimestamp());
                asch.setAidFromAsset(asch);
            }
            else if(asset.getType()==UserAsset.TYPE_GATEWAY){
                name = asset.getName();
                asch.setName(asset.getName());
                asch.setDesc(asset.getDesc());
                asch.setPrecision(asset.getPrecision());
                asch.setRevoked(asset.getRevoked());
                asch.setType(AschAsset.TYPE_GATEWAY);
                asch.setGateway(asset.getGateway());
                asch.setAidFromAsset(asch);
            }
            asch.setBalance("0");
            asch.setShowState(AschAsset.STATE_UNSETTING);
            if (!TextUtils.isEmpty(name))
                aschAssetMap.put(name,asch);
        }

        //生成余额map
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

            if (aschAssetMap.containsKey(name)){
                AschAsset asset = aschAssetMap.get(name);
                asset.setBalance(balance.getBalance());
                asset.setTrueBalance(balance.getRealBalance());
                aschAssetMap.put(name,asset);
            }
        }
        return aschAssetMap;




        //整理出一个带余额的aschList
        //写入过程单独抽出来
        /**
         * for (AschAsset remote:aschList){
         *             if(!assetHashMap.containsKey(remote.getName()))
         *                 addAsset(remote);
         *         }
         *         getRealm().beginTransaction();
         *         for (AschAsset aschAsset:assetHashMap.values()){
         *             if (balanceHashMap.containsKey(aschAsset.getName())){
         *                 int savedState = aschAsset.getShowState();
         *                 Balance balance = balanceHashMap.get(aschAsset.getName());
         *                 aschAsset.setBalance(balance.getBalance());
         *                 aschAsset.setTrueBalance(balance.getRealBalance());
         *                 if (savedState!=AschAsset.STATE_UNSHOW)
         *                     aschAsset.setShowState(AschAsset.STATE_SHOW);
         *             }
         *         }
         *         getRealm().commitTransaction();
         */

    }

//    //操作余额与全部资产,包括阿希
//    public rx.Observable<List<AschAsset>> allBalanceAndAssetsObservable(String address){
//        rx.Observable allAssetObservable = balanceAndAssetsObservable(address);
//        rx.Observable xasObservable = xasObservable();
//        rx.Observable combinedObservable = rx.Observable.combineLatest(allAssetObservable, xasObservable, new Func2<List<Object>, List<Balance>, List<AschAsset>>() {
//            @Override
//            public List<AschAsset> call(List<Object> aschAssets, List<Balance> balances) {
//
//                setBalanceToRealm(balances);
//
//                return null;
//            }
//        });
//        return  combinedObservable;
//    }


    //操作余额与全部资产
    public rx.Observable<List<AschAsset>> balanceAndAssetsObservable(String address){
        rx.Observable allAssetObservable = allAssetsObservable();
        rx.Observable balanceObservable = balanceObservable(address);
        rx.Observable combinedObservable = rx.Observable.combineLatest(allAssetObservable, balanceObservable, new Func2<List<UserAsset>, List<Balance>, LinkedHashMap<String,AschAsset>>() {
            @Override
            public LinkedHashMap<String,AschAsset> call(List<UserAsset> aschAssets, List<Balance> balances) {

                return  setAssetToRealm(aschAssets,balances);
            }
        });
        return  combinedObservable;
    }


//    //读取阿希余额
//    public  rx.Observable<AschAsset> xasObservable(){
//        return rx.Observable.create(new rx.Observable.OnSubscribe<AschAsset>(){
//            @Override
//
//            public void call(Subscriber<? super AschAsset> subscriber) {
//
//                Account account = AccountsManager.getInstance().getCurrentAccount();
//                String ads = account.getAddress();
//                String xasBalance = account.getFullAccount().getAccount().getBalance();
//                long locked = account.getFullAccount().getAccount().getLockedAmount();
//                long longBalance = Long.parseLong(xasBalance);
//                long longTotal = locked+longBalance;
//                String xasTotal = getTotalBalanceString(longTotal,AppConstants.PRECISION);
//                AschAsset aschAsset = new AschAsset();
//                aschAsset.setBalance(xasBalance);
//                aschAsset.setXasTotal(xasTotal);
//                aschAsset.setName(AppConstants.XAS_NAME);
//                aschAsset.setType(AschAsset.TYPE_XAS);
//                aschAsset.setShowState(AschAsset.STATE_SHOW);
//                aschAsset.setPrecision(AppConstants.PRECISION);
//                aschAsset.setTrueBalance ((float) (Double.parseDouble(aschAsset.getBalance())/(Math.pow(10,AppConstants.PRECISION))));
//
////                LogUtils.iTag(TAG,);
//
//                subscriber.onNext(aschAsset);
//                subscriber.onCompleted();
////                }else{
////                    subscriber.onError(resultUIA.getException());
////                }
//            }
//        });
//    }

    //读取写入全部资产
    public rx.Observable<List<Object>> allAssetsObservable(){
        rx.Observable uiaAssetObv = uiaObservable();
        rx.Observable gatewayObv = gateWayObservable();
        rx.Observable combinedObservable = rx.Observable.combineLatest(uiaAssetObv, gatewayObv, new Func2<List<UIAAsset>, List<GatewayAsset>, List<UserAsset>>() {
            @Override
            public List<UserAsset> call(List<UIAAsset> uiaAssets, List<GatewayAsset> gatewayAssets) {

                return initAllAssets(uiaAssets,gatewayAssets);
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
