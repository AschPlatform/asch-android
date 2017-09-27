package asch.so.wallet.presenter;

import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import so.asch.sdk.AschHelper;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.SecurityException;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AccountCreatePresenter {

    /**
     * 生成seed
     * @return
     */
    public String generateSeed(){
        return null;
    }

    /**
     * 创建账户
     * @param seed
     * @param name
     * @param passwd
     * @param hint
     */
    public void createAccount(String seed, String name, String passwd, String hint){

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
            AccountsDao.getInstance().addAccount(account);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
