package asch.io.wallet.crypto;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.accounts.Wallet;
import asch.io.wallet.model.entity.Account;
import so.asch.sdk.codec.Encoding;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.SecurityException;

import static asch.io.wallet.crypto.AesCbcWithIntegrity.*;

/**
 * Created by kimziv on 2017/9/25.
 * 用户信息加密解密简单类
 */

public class AccountSecurity {
    private  static  final  String TAG=AccountSecurity.class.getSimpleName();

    private static final byte[] salt = "Asch_Wallet_Security_Initialize_".getBytes();




    public static String encryptPwd(String pwd){
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

    public static String encryptSecondPwd(String pwd,String secondPwd){
        try {
            SecretKeys key =generateKeyFromPassword(pwd,saltString(salt));
            AesCbcWithIntegrity.CipherTextIvMac civ =encrypt(secondPwd, key);
            return civ.toString();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptSecondPwd(String pwd,String encryptedSecondPwd){
        try {
            SecretKeys key =generateKeyFromPassword(pwd,saltString(salt));
            CipherTextIvMac civ =new CipherTextIvMac(encryptedSecondPwd);
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
     * 加密钱包
     * @param pwd
     * @return
     */
    public static Wallet encryptWallet(Wallet wallet, String pwd){
        wallet.setEncryptPassword(encryptPwd(pwd));
        String pwdKey = genWalletPwdKey(pwd);
        Wallet.getInstance().setPwdKey(pwdKey);
        return wallet;
    }


    public static Boolean checkSecondPassword(String secondPwd){
        try {
            PublicKey publicKey = AschFactory.getInstance().getSecurity().generateKeyPair(secondPwd).getPublic();
            String strKey = AschFactory.getInstance().getSecurity().encodePublicKey(publicKey);
            if (AccountsManager.getInstance().getCurrentAccount().getFullAccount().getAccount().getSecondPublicKey().equals(strKey)){
                return true;
            }else
                return false;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
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


    public static String decryptSecret(String passwd){
        try {
            SecretKeys key =generateKeyFromPassword(genWalletPwdKey(passwd),saltString(salt));
            CipherTextIvMac civ =new CipherTextIvMac(AccountsManager.getInstance().getCurrentAccount().getEncryptSeed());
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
