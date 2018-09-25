package asch.so.wallet.accounts;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.LogUtils;

import org.bitcoinj.crypto.MnemonicCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConfig;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.CoreAsset;
import asch.so.wallet.model.entity.UIAAsset;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.security.Bip39;

/**
 * Created by kimziv on 2017/10/17.
 */

public class Wallet {

    private static final  String TAG=Wallet.class.getSimpleName();
    private static Wallet instance=null;
    private Context context;
    private  MnemonicCode mnemonicCode;
    private LinkedHashMap<String,BaseAsset> allssets;
    private static final String UIA_ASSETS_CACHE_KEY=AppConfig.getNodeURL()+"UIA_ASSETS_CACHE_KEY";

    public static final String ACCOUNT_LOCK_TIMEOUT_CACHE_KEY=AppConfig.getNodeURL()+"ACCOUNT_LOCK_TIMEOUT_CACHE_KEY";

    public Wallet(Context ctx) {
        this.context=ctx;
        this.allssets=new LinkedHashMap<>();
        this.allssets.put("XAS",new CoreAsset());
        loadAssets(true,new Wallet.OnLoadAssetsListener() {
            @Override
            public void onLoadAllAssets(LinkedHashMap<String, BaseAsset> assetsMap, Throwable exception) {
                LogUtils.dTag(TAG,"all assets:"+assetsMap.toString());
            }
        });

        try {
            InputStream wis = ctx.getResources().getAssets().open(Bip39.BIP39_WORDLIST_FILENAME);
            if(wis != null) {
                this.mnemonicCode = new MnemonicCode(wis, Bip39.BIP39_ENGLISH_SHA256);
                wis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Wallet getInstance(){
        return instance;
    }

    public static void init(Context ctx){
        instance=new Wallet(ctx);
    }

    public  Boolean isSetPwd(){
        if(AppConfig.getWalletPwd()!=null && !AppConfig.getWalletPwd().isEmpty()) {
            return true;
        }else {
            return false;
        }
    }


    private String getEncryptPassword(){
        return AppConfig.getWalletPwd();
    }

    public void setEncryptPassword(String encryptPwd){
        AppConfig.putWalletPwd(encryptPwd);
    }

    public void setPwdKey(String encryptPwd){
        AppConfig.putPwdKey(encryptPwd);
    }

    public String getPwdKey(){
        return AppConfig.getPwdKey();
    }

    public boolean checkPassword(String pwd){
        if (pwd==null || pwd.length()==0)
            return false;
        String decryptPasswd= AccountSecurity.decryptPassword(getEncryptPassword(),pwd);
        return pwd.equals(decryptPasswd);
    }

    public  String decryptPassword(String pwd){
        if (pwd==null || pwd.length()==0)
            return null;
        String decryptPassword= AccountSecurity.decryptPassword(getEncryptPassword(),pwd);
        return decryptPassword;
    }



    public MnemonicCode getMnemonicCode() {
        return mnemonicCode;
    }

    public LinkedHashMap<String, BaseAsset> getAllssets() {

        return allssets;
    }



    public void setAllssets(LinkedHashMap<String, BaseAsset> allssets) {
        this.allssets = allssets;
    }


    //读取所有资产种类，合并获取UIA与Gateway资产
    public  rx.Observable<List<BaseAsset>> createLoadAllAssetsObservable(boolean ignoreCache){
        return rx.Observable.create(new Observable.OnSubscribe<List<BaseAsset>>(){
            @Override
            public void call(Subscriber<? super List<BaseAsset>> subscriber) {
//                String cacheJson=CacheUtils.getInstance().getString(UIA_ASSETS_CACHE_KEY);
//                if (!ignoreCache && !TextUtils.isEmpty(cacheJson)){
//                    JSONObject resultJSONObj=JSONObject.parseObject(cacheJson);
//                    JSONArray balanceJsonArray=resultJSONObj.getJSONArray("assets");
//                    List<BaseAsset> assets= JSON.parseArray(balanceJsonArray.toJSONString(),BaseAsset.class);
//                    subscriber.onNext(assets);
//                    subscriber.onCompleted();
//                    return;
//                }
                AschResult resultUIA = AschSDK.UIA.getAssets(100,0);
                AschResult resultGateway = AschSDK.UIA.getGatewayAssets(100,0);

                LogUtils.iTag(TAG,resultUIA.getRawJson());
                if (resultUIA.isSuccessful() && resultGateway.isSuccessful()){
                    ArrayList<BaseAsset> allAssets = new ArrayList<>();

                    //解析UIA资产
                    String rawJson=resultUIA.getRawJson();
                    JSONObject resultJSONObj=JSONObject.parseObject(rawJson);
                    JSONArray assetJsonArray=resultJSONObj.getJSONArray("assets");
                    List<BaseAsset> assetsUIA= JSON.parseArray(assetJsonArray.toJSONString(),BaseAsset.class);
                    for (BaseAsset as:assetsUIA)
                        as.setType(BaseAsset.TYPE_UIA);
                    allAssets.addAll(assetsUIA);

                    //解析Gateway资产
                    String rawJsonGateway=resultGateway.getRawJson();
                    JSONObject resultJSONObjGateway=JSONObject.parseObject(rawJsonGateway);
                    JSONArray assetJsonArrayGateway=resultJSONObjGateway.getJSONArray("currencies");
                    for (Object obj :
                            assetJsonArrayGateway) {
                        BaseAsset assetGateway=new BaseAsset();
                        JSONObject jsonObj=(JSONObject)obj;
                        assetGateway.setName(jsonObj.getString("symbol"));
                        assetGateway.setPrecision(jsonObj.getIntValue("precision"));
                        assetGateway.setGateway(jsonObj.getString("gateway"));
                        assetGateway.setDesc(jsonObj.getString("desc"));
                        assetGateway.setType(BaseAsset.TYPE_GATEWAY);
                        allAssets.add(assetGateway);
                    }

                    CacheUtils.getInstance().put(UIA_ASSETS_CACHE_KEY,rawJson,AppConstants.DEFAULT_CACHE_TIMEOUT);
                    subscriber.onNext(allAssets);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(resultUIA.getException());
                }
            }
        });
    }


    public Subscription loadAssets(boolean ignoreCache, OnLoadAssetsListener callback) {
        Observable uiaOervable = createLoadAllAssetsObservable(ignoreCache);
        Subscription subscription= uiaOervable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<BaseAsset>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                       // view.displayError(new Throwable("转账失败"));
                        if (callback!=null){
                            callback.onLoadAllAssets(allssets,new Throwable(e!=null?e.getMessage():context.getString(R.string.get_uia_error)));
                        }
                    }

                    @Override
                    public void onNext(List<BaseAsset> assets) {
                        for (BaseAsset baseAsset :
                                assets) {
                            allssets.put(baseAsset.getName(),baseAsset);
                        }
                        if (callback!=null){
                            callback.onLoadAllAssets(allssets,null);
                        }
                    }
                });
        return subscription;
        //subscriptions.add(subscription);
    }

    public  interface OnLoadAssetsListener{

        void onLoadAllAssets(LinkedHashMap<String,BaseAsset> assetsMap, Throwable exception);
    }
}
