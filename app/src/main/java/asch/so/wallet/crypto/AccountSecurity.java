package asch.so.wallet.crypto;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import asch.so.wallet.AppConfig;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.model.entity.Account;
import so.asch.sdk.codec.Encoding;
import so.asch.sdk.security.DefaultSecurityStrategy;

import static asch.so.wallet.crypto.AesCbcWithIntegrity.*;

/**
 * Created by kimziv on 2017/9/25.
 * 用户信息加密解密简单类
 */

public class AccountSecurity {
    private  static  final  String TAG=AccountSecurity.class.getSimpleName();

    private static final byte[] salt = "Asch_Wallet_Security_Initialize_".getBytes();

    private static String encryptPwd(String pwd){
        try {
            SecretKeys key =generateKeyFromPassword(pwd,saltString(salt));
            AesCbcWithIntegrity.CipherTextIvMac civ =encrypt(pwd, key);
            return civ.toString();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加密钱包
     * @param pwd
     * @return
     */
    public static Wallet encryptWallet(Wallet wallet, String pwd){
        wallet.setEncryptPasswd(encryptPwd(pwd));
        String pwdKey = genWalletPwdKey(pwd);
        Wallet.getInstance().setPwdKey(pwdKey);
        return wallet;
    }


    /**
     * 解密钱包密码
     * @param pwd
     * @return
     */
    public static String decryptPassword(String encryptPasswd, String pwd){
        try {
            SecretKeys key =generateKeyFromPassword(pwd,saltString(salt));
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


    /**
     * 生成pwdKey，用于加密seed
     * @param pwd
     * @return
     */
    public static String genWalletPwdKey(String pwd){
        try {
            MessageDigest digest =  MessageDigest.getInstance("SHA-256");
            digest.update(pwd.getBytes());
            return new String(Encoding.hex(digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加密用户信息
     * @param account
     * @return
     */
    public static Account encryptAccount(Account account){
        try {
            SecretKeys key =generateKeyFromPassword(Wallet.getInstance().getPwdKey(),saltString(salt));
            AesCbcWithIntegrity.CipherTextIvMac civ =encrypt(account.getSeed(), key);
            account.setEncryptSeed(civ.toString());
            return account;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String decryptSecret(String encryptSecret, String passwd){
        try {
            SecretKeys key =generateKeyFromPassword(genWalletPwdKey(passwd),saltString(salt));
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
