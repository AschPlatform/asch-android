package asch.io.wallet.accounts;

import android.content.Context;

import org.bitcoinj.crypto.MnemonicCode;

import java.io.IOException;
import java.io.InputStream;

import asch.io.wallet.AppConfig;
import asch.io.wallet.crypto.AccountSecurity;
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


    public String getEncryptPassword(){

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
