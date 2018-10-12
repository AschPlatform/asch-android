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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConfig;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.AschAsset;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.CoreAsset;
import asch.so.wallet.model.entity.FullAccount;
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

    private static final String UIA_ASSETS_CACHE_KEY=AppConfig.getNodeURL()+"UIA_ASSETS_CACHE_KEY";

    public static final String ACCOUNT_LOCK_TIMEOUT_CACHE_KEY=AppConfig.getNodeURL()+"ACCOUNT_LOCK_TIMEOUT_CACHE_KEY";

    public Wallet(Context ctx) {
        this.context=ctx;


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

}
