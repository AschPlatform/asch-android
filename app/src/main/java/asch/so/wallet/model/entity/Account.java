package asch.so.wallet.model.entity;

import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import asch.so.wallet.AppConstants;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.AssetManager;
import asch.so.wallet.crypto.AccountSecurity;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kimziv on 2017/9/21.
 */

public class Account extends RealmObject{

    //用户密码，暂时存储在数据库里，后续放入tee环境里
    private String seed;
    //钱包别名
    private String name;
    //@PrimaryKey
    //地址
//    @PrimaryKey
    private String address;
    //公钥
    @PrimaryKey
    private String publicKey;
    //账户临时存储密码
    @Ignore
    private String tmpPasswd;
    private String encryptSeed;
    private String secondSecret;
    //是否已备份
    private boolean backup;
    //是否保存二级密码
    private int saveSecondPwdState;


    @Ignore
    @JSONField(serialize=false)
    private FullAccount fullAccount;

    public FullAccount getFullAccount() {
        return fullAccount;
    }

    public void setFullAccount(FullAccount fullAccount) {
        this.fullAccount = fullAccount;
        AschAsset aschAsset = new AschAsset();
        aschAsset.setBalance(fullAccount.getAccount().getBalance());
        aschAsset.setAddress(address);
        aschAsset.setName(AppConstants.XAS_NAME);
        aschAsset.setType(AschAsset.TYPE_XAS);
        aschAsset.setShowState(AschAsset.STATE_SHOW);
        aschAsset.setPrecision(AppConstants.PRECISION);
        aschAsset.setTrueBalance ((float) (Double.parseDouble(aschAsset.getBalance())/(Math.pow(10,AppConstants.PRECISION))));
        AssetManager.getInstance().addAsset(aschAsset);
    }



    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEncryptSeed() {
        return encryptSeed;
    }

    public void setEncryptSeed(String encryptSeed) {
        this.encryptSeed = encryptSeed;
    }



    public String toJSON(){
      return  JSON.toJSONString(this);
    }


    public  String decryptSecret(String passwd){
        if (passwd==null || passwd.length()==0)
            return null;
        String decryptSecret= AccountSecurity.decryptSecret(getEncryptSeed(),passwd);
        return decryptSecret;
    }

    public static String decryptSecret(String passwd, String encryptSecret){
        if (passwd==null || passwd.length()==0)
            return null;
        String decryptSecret= AccountSecurity.decryptSecret(encryptSecret,passwd);
        return decryptSecret;
    }

    public @States int getSaveSecondPasswordState(){
        return saveSecondPwdState;
    }


    public void setSaveSecondPasswordState(@States int state) {
        saveSecondPwdState = state;
    }



    @IntDef({STATE_SUGGEST,STATE_REMEMBER,STATE_FORGET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface States {}
    public static final int STATE_SUGGEST = 0;
    public static final int STATE_REMEMBER = 1;
    public static final int STATE_FORGET = -1;


    public boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    public boolean hasSecondSecret(){
        if (getFullAccount()!=null && getFullAccount().getAccount()!=null)
        {
           String secondPubkey = getFullAccount().getAccount().getSecondPublicKey();
            return !(TextUtils.isEmpty(secondPubkey));
        }
        return false;
    }

    public boolean hasLockCoins(){
        if (getFullAccount()!=null && getFullAccount().getAccount()!=null)
        {
//            long lockHeight = getFullAccount().getAccount().getLockHeight();
//            long currentHeight = getFullAccount().getLatestBlock().getHeight();
//            return lockHeight>currentHeight;
            return  getFullAccount().getAccount().isLocked();
        }
        return false;
    }


    public String getSecondSecret(String password) {
        return AccountSecurity.decryptSecondPwd(password,secondSecret);
    }

    public void setSecondSecret(String password,String secondSecret) {
        this.secondSecret = AccountSecurity.encryptSecondPwd(password,secondSecret);;
    }

    public String getTmpPasswd() {
        return tmpPasswd;
    }

    public void setTmpPasswd(String tmpPasswd) {
        this.tmpPasswd = tmpPasswd;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this)
        {
            return true;
        }
        else if (obj instanceof Account) {
            Account accountObj = (Account) obj;
            return (accountObj.getPublicKey().equals(this.getPublicKey()));
        }
        return false;
    }

    public long getXASLongBalance(){
        if (AssetManager.getInstance().getBalances()!=null && AssetManager.getInstance().getBalances().size()>0)
        {
            AschAsset balance=AssetManager.getInstance().getBalances().get(0);
            long amount =balance.getLongBalance();
            return amount;
        }
        return 0;
    }
}
