package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;

import asch.so.wallet.AppConfig;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountCreateContract;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import so.asch.sdk.AschHelper;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.Bip39;
import so.asch.sdk.security.SecurityException;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AccountCreatePresenter implements AccountCreateContract.Presenter{
    private  final  static  String TAG=AccountCreatePresenter.class.getSimpleName();
    private AccountCreateContract.View view;

    private Context context;

    public  AccountCreatePresenter(Context context, AccountCreateContract.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
    }

    @Override
    public void storeAccount(String seed, String name, String password, String hint) {

        createAccount(seed,name,password,hint);
    }

    @Override
    public void generateSeed() {
        try {
            InputStream wis = context.getResources().getAssets().open(Bip39.BIP39_WORDLIST_FILENAME);
            String words = Bip39.generateMnemonic(wis,12);
            Log.i(TAG, "words:"+words);
            this.view.resetSeed(words);
        }catch (Exception ex){
            // TODO: 2017/11/2
        }
    }


    /**
     * 创建账户
     * @param seed
     * @param name
     * @param passwd
     * @param hint
     */
    private void createAccount(String seed, String name, String passwd, String hint){

        try {
            String pubKey = AschSDK.Helper.getPublicKey(seed);
            String address = AschFactory.getInstance().getSecurity().getAddress(pubKey);

            Account account =new Account();
            account.setSeed(seed);
            account.setPublicKey(pubKey);
            account.setAddress(address);
            account.setName(name);
            account.setPasswd(passwd);
            account.setHint(hint);
            AccountSecurity.encryptAccount(account,passwd);
            AccountsManager.getInstance().addAccount(account);
            AppConfig.putLastAccountAddress(account.getAddress());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
