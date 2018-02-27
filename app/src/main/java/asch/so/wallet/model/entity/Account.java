package asch.so.wallet.model.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

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

    //账户加密密码
    private String passwd;
    //密码提示
    private  String hint;

    private String encryptSeed;

    private String encryptPasswd;

    private String secondSecret;
    private String enryptSecondSecret;
    //是否已备份
    private boolean backup;

    @Ignore
    @JSONField(serialize=false)
    private FullAccount fullAccount;

    public FullAccount getFullAccount() {
        return fullAccount;
    }

    public void setFullAccount(FullAccount fullAccount) {
        this.fullAccount = fullAccount;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public String getEncryptPasswd() {
        return encryptPasswd;
    }

    public void setEncryptPasswd(String encryptPasswd) {
        this.encryptPasswd = encryptPasswd;
    }

    public String toJSON(){
      return  JSON.toJSONString(this);
    }

    public boolean checKPassword(String passwd){
        if (passwd==null || passwd.length()==0)
            return false;
        String decryptPasswd= AccountSecurity.decryptPassword(getEncryptPasswd(),passwd);
        return passwd.equals(decryptPasswd);
    }

    public String decryptSecret(String passwd){
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

    public boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    public boolean hasSecondSecret(){
        if (getFullAccount()!=null && getFullAccount().getAccount()!=null)
        {
            return getFullAccount().getAccount().isSecondSignature();
        }
        return false;
    }


    public boolean hasLockCoins(){
        if (getFullAccount()!=null && getFullAccount().getAccount()!=null)
        {
            long lockHeight = getFullAccount().getAccount().getLockHeight();
            long currentHeight = getFullAccount().getLatestBlock().getHeight();
            return lockHeight>currentHeight;
        }
        return false;
    }


    public String getSecondSecret() {
        return secondSecret;
    }

    public void setSecondSecret(String secondSecret) {
        this.secondSecret = secondSecret;
    }

    public String getEnryptSecondSecret() {
        return enryptSecondSecret;
    }

    public void setEnryptSecondSecret(String enryptSecondSecret) {
        this.enryptSecondSecret = enryptSecondSecret;
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
}
