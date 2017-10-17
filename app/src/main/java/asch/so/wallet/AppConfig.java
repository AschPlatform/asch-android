package asch.so.wallet;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

/**
 * Created by kimziv on 2017/10/17.
 * key-value 安全存储类
 */

public class AppConfig {

    private static final String  LAST_ACCOUNT_ADDRESS_KEY="lastAccountAddress";
    private static final String  LAST_ACCOUNT_PUBLIC_KEY_KEY="lastAccountPublicKey";
    private static String lastAccountAddress=null;
    private static String lastAccountPublicKey=null;



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

    public static void clear(){
        Hawk.deleteAll();
        lastAccountAddress=null;
        lastAccountPublicKey=null;
    }
}
