package asch.so.wallet.accounts;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import asch.so.wallet.AppConfig;
import asch.so.wallet.activity.module.AccountsModule;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/10/17.
 * 账户管理类
 */

public class AccountsManager {

    private static AccountsManager accountsManager=null;


   // private Context context;

    //当前账户
    private Account currentAccount=null;
    //所有账户
    private ArrayList<Account> accounts=null;



    public AccountsManager getInstance(Context context){
        if (accountsManager==null){
            accountsManager=new AccountsManager();
        }
        return accountsManager;
    }

    public AccountsManager() {
        //this.context = context;
        this.accounts =new ArrayList<>();
        loadAccounts();
    }

    private void loadAccounts(){
        this.accounts.addAll(AccountsDao.getInstance().queryAllSavedAccounts());
        if (this.accounts!=null && this.accounts.size()>0){
            String lastAddress=AppConfig.getLastAccountAddress();
            if (lastAddress!=null){
                for (Account account :
                        accounts) {
                    if (account.getAddress().equals(lastAddress)) {
                       currentAccount=account;
                      }
                    }
            }
            if (currentAccount==null){
                currentAccount=accounts.get(0);
            }
        }
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    /**
     * 查询所有账户
     * @return
     */
    public List<Account> queryAccounts(){
        if (accounts.size()==0){
            accounts.addAll(AccountsDao.getInstance().queryAllSavedAccounts());
        }
        return accounts;
    }

    /**
     * 添加账户
     * @param account
     */
    public void addAccount(Account account){
        accounts.add(account);
        AccountsDao.getInstance().addAccount(account);
        currentAccount=account;
    }

    /**
     * 删除账户
     * @param account
     */
    public void removeAccount(Account account){
        accounts.remove(account);
        if (currentAccount==account){
            if (accounts.size()>0)
            {
                currentAccount=accounts.get(0);
            }else {
                currentAccount=null;
            }
        }
    }

    /**
     * 根据地址或公钥删除账户
     * @param addressOrPublickKey
     */
    public void removeAccount(String addressOrPublickKey){
        Account account=AccountsDao.getInstance().queryAccount(addressOrPublickKey);
        accounts.remove(account);
        removeAccount(account);
    }

    /**
     * 删除当前用户
     */
    public void removeAccount(){
        if (currentAccount!=null){
           removeAccount(currentAccount);
        }
    }

    /**
     * 更新账户
     * @param account
     */
    public void updateAccount(Account account){
       int index = accounts.indexOf(account);
        if (index!=-1)
        {
            accounts.set(index,account);
            AccountsDao.getInstance().updateAccount(account);
        }else {
            //todo throw exception
        }
    }

    /**
     * 更新制定地址的账户别名
     * @param addressOrPublickKey
     * @param name
     */
    public void updateAccount(String addressOrPublickKey, String name){
        for (Account account:accounts
             ) {
            if (account.getAddress().equals(addressOrPublickKey)){
                account.setName(name);
                AccountsDao.getInstance().updateAccount(account);
            }
        }
    }

    /**
     * 更新当前用户的别名
     * @param name
     */
    public void updateAccount(String name){
        if (currentAccount!=null){
            currentAccount.setName(name);
            updateAccount(currentAccount.getAddress(),name);
        }
    }

}
