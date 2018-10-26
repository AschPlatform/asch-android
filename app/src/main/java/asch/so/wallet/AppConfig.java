package asch.so.wallet;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by kimziv on 2017/10/17.
 * key-value 安全存储类
 */

public class AppConfig {

    private static final String  LAST_ACCOUNT_ADDRESS_KEY="lastAccountAddressKey";
    private static final String  LAST_ACCOUNT_PUBLIC_KEY_KEY="lastAccountPublicKeyKey";
    private static String lastAccountAddress=null;
    private static String lastAccountPublicKey=null;
    private static final  String LANGUAGE_KEY="languagekey";
    private static String language=null;
    private static final  String NODE_URL_KEY="nodeURLKey";
    private static String nodeURL=null;
    private static final String WALLET_PWD = "walletPwd";
    private static final String PWD_KEY = "pwdKey";


    public static void init(Context context){
        Hawk.init(context).build();
    }
    /**
     *lastAccountAddress get, put, delete
     *
     */



    public static String getLastAccountAddress(){
        if (lastAccountAddress==null){
            lastAccountAddress= Hawk.get(LAST_ACCOUNT_ADDRESS_KEY);
        }

        return lastAccountAddress;
    }

    public static void putLastAccountAddress(String address){
        Hawk.put(LAST_ACCOUNT_ADDRESS_KEY,address);
        lastAccountAddress=address;
    }

    public static void deleteLastAccountAddress(){
        if (Hawk.contains(LAST_ACCOUNT_ADDRESS_KEY))
        {
            Hawk.delete(LAST_ACCOUNT_ADDRESS_KEY);
        }
        lastAccountAddress=null;
    }

    /**
     *lastAccountPublicKey get, put, delete
     *
     */

    public static String getlastAccountPublicKey(){
        if (lastAccountPublicKey==null){
            lastAccountPublicKey= Hawk.get(LAST_ACCOUNT_PUBLIC_KEY_KEY);
        }
        return lastAccountPublicKey;
    }

    public static void putLastAccountPublicKey(String publicKey){
        Hawk.put(LAST_ACCOUNT_PUBLIC_KEY_KEY,publicKey);
        lastAccountPublicKey=publicKey;
    }

    public static void deleteLastAccountPublicKey(){
        if (Hawk.contains(LAST_ACCOUNT_PUBLIC_KEY_KEY))
        {
            Hawk.delete(LAST_ACCOUNT_PUBLIC_KEY_KEY);
        }
        lastAccountPublicKey=null;
    }

    /**
     *language get, put, delete
     *
     */

    public static String getLanguage(){
        if (language==null){
            language= Hawk.get(LANGUAGE_KEY);
        }
        return language;
    }

    public static void putLanguage(String lang){
        Hawk.put(LANGUAGE_KEY,lang);
        language=lang;
    }

    public static void deleteLanguage(){
        if (Hawk.contains(LANGUAGE_KEY))
        {
            Hawk.delete(LANGUAGE_KEY);
        }
        language=null;
    }

    /**
     *node url get, put, delete
     *
     */

    public static String getNodeURL(){
        if (nodeURL==null){
            nodeURL= Hawk.get(NODE_URL_KEY);
        }
        return nodeURL;
    }

    public static void putNodeURL(String url){
        Hawk.put(NODE_URL_KEY,url);
        nodeURL=url;
    }

    public static void deleteNodeURL(){
        if (Hawk.contains(NODE_URL_KEY))
        {
            Hawk.delete(NODE_URL_KEY);
        }
        nodeURL=null;
    }

    public static void putWalletPwd(String pwd){
        Hawk.put(WALLET_PWD,pwd);
    }

    public static String getWalletPwd(){
        return Hawk.get(WALLET_PWD);
    }

    public static void deleteWalletPwd(){
        if (Hawk.contains(WALLET_PWD))
        {
            Hawk.delete(WALLET_PWD);
        }
    }

    public static void putPwdKey(String pwd){
        Hawk.put(PWD_KEY,pwd);
    }

    public static String getPwdKey(){
        return Hawk.get(PWD_KEY);
    }

    public static void clear(){
        Hawk.deleteAll();
        lastAccountAddress=null;
        lastAccountPublicKey=null;
        language=null;
    }
}
