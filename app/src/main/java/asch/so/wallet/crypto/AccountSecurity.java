package asch.so.wallet.crypto;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import asch.so.wallet.model.entity.Account;

import static asch.so.wallet.crypto.AesCbcWithIntegrity.*;

/**
 * Created by kimziv on 2017/9/25.
 * 用户信息加密解密简单类
 */

public class AccountSecurity {
    private  static  final  String TAG=AccountSecurity.class.getSimpleName();

    private static final byte[] salt = "Asch_Wallet_Security_Initialize_".getBytes();

    /**
     * 加密用户信息
     * @param account
     * @param passwd
     * @return
     */
    public static Account encryptAccount(Account account,String passwd){
        try {
            SecretKeys key =generateKeyFromPassword(passwd,saltString(salt));
            AesCbcWithIntegrity.CipherTextIvMac civ =encrypt(account.getSeed(), key);
            Log.i(TAG, "Encrypted: " + civ.toString());
            account.setEncryptSeed(civ.toString());
            AesCbcWithIntegrity.CipherTextIvMac civ2 =encrypt(passwd, key);
            account.setEncryptPasswd(civ2.toString());
            return account;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密用户信息
     * @param account
     * @param passwd
     * @return
     */
    public static Account decryptAccount(Account account, String passwd){

        try {
            SecretKeys key =generateKeyFromPassword(passwd,saltString(salt));
            CipherTextIvMac civ =new CipherTextIvMac(account.getEncryptSeed());
            String decrytText = decryptString(civ, key);
            account.setSeed(decrytText);

            CipherTextIvMac civ2 =new CipherTextIvMac(account.getEncryptPasswd());
            String decrytText2 = decryptString(civ2, key);
            account.setSeed(decrytText2);

            return account;

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptPassword(String encryptPasswd, String passwd){

        try {
            SecretKeys key =generateKeyFromPassword(passwd,saltString(salt));
            CipherTextIvMac civ =new CipherTextIvMac(encryptPasswd);
            String decrytText = decryptString(civ, key);
            return  decrytText;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptSecret(String encryptSecret, String passwd){

        try {
            SecretKeys key =generateKeyFromPassword(passwd,saltString(salt));
            CipherTextIvMac civ =new CipherTextIvMac(encryptSecret);
            String decrytText = decryptString(civ, key);
            return  decrytText;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
