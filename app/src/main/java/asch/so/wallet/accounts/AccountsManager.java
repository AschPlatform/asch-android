package asch.so.wallet.accounts;

import android.accounts.AccountManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConfig;
import asch.so.wallet.AppConstants;
import asch.so.wallet.model.db.dao.AccountsDao;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.FullAccount;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;

/**
 * Created by kimziv on 2017/10/17.
 * 账户管理类
 */

public class AccountsManager extends Observable {

    private static final String TAG = AccountManager.class.getSimpleName();

    private static AccountsManager accountsManager = null;

    //当前账户
    private Account currentAccount = null;
    //所有账户
    private ArrayList<Account> accounts = null;
    //删除时操作的账户
    private Account delAccount = null;


    public static AccountsManager getInstance() {
        if (accountsManager == null) {
            accountsManager = new AccountsManager();
        }
        return accountsManager;
    }

    public AccountsManager() {
        this.accounts = new ArrayList<>();
        loadAccounts();
    }

    private void loadAccounts() {
        this.accounts.addAll(AccountsDao.getInstance().queryAllSavedAccounts());
        if (this.accounts != null && this.accounts.size() > 0) {
            String lastPubkey = AppConfig.getlastAccountPublicKey();
            if (lastPubkey != null) {
                for (Account account :
                        accounts) {
                    if (account.getPublicKey().equals(lastPubkey)) {
                        setCurrentAccount(account);
                    }
                }
            }
            if (currentAccount == null) {
                setCurrentAccount(accounts.get(0));
            }
        }
    }

    public Account getDelAccount() {
        return delAccount;
    }

    public void setDelAccount(Account account){
        this.delAccount = account;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {

        this.currentAccount = currentAccount;
        loadFullAccount(currentAccount);
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * 查询所有账户
     *
     * @return
     */
    public List<Account> queryAccounts() {
        if (accounts.size() == 0) {
            accounts.addAll(AccountsDao.getInstance().queryAllSavedAccounts());
        }
        return accounts;
    }

    public int getAccountsCount(){
        List<Account> accounts=queryAccounts();
        return accounts==null?0:accounts.size();
    }

    /**
     * 添加账户
     *
     * @param account
     */
    public void addAccount(Account account) {
        accounts.add(account);
        AccountsDao.getInstance().addAccount(account);
        setCurrentAccount(account);
    }

    /**
     * 删除账户
     *
     * @param account
     */
    public void removeAccount(Account account) {
        accounts.remove(account);
        if (currentAccount.getAddress().equals(account.getAddress())) {
            if (accounts.size() > 0) {
                setCurrentAccount(accounts.get(0));
            } else {
                setCurrentAccount(null);
            }
        }
        delAccount = null;
        AccountsDao.getInstance().removeAccount(account);
    }

    /**
     * 根据地址或公钥删除账户
     *
     * @param addressOrPublickKey
     */
    public void removeAccount(String addressOrPublickKey) {
        Account account = AccountsDao.getInstance().queryAccount(addressOrPublickKey);
        accounts.remove(account);
        removeAccount(account);
    }

    /**
     * 删除当前用户
     */
    public void removeCurrentAccount() {
        if (currentAccount != null) {
            removeAccount(currentAccount);
        }
    }

    /**
     * 更新账户
     *
     * @param account
     */
    public void updateAccount(Account account) {
        int index = accounts.indexOf(account);
        if (index != -1) {
            accounts.set(index, account);
            AccountsDao.getInstance().updateAccount(account);
        } else {
            //todo throw exception
        }
    }

    /**
     * 更新制定地址的账户别名
     *
     * @param addressOrPublickKey
     * @param name
     */
    public void updateAccount(String addressOrPublickKey, String name) {
        for (Account account : accounts
                ) {
            if (account.getAddress().equals(addressOrPublickKey)) {
                account.setName(name);
                AccountsDao.getInstance().updateAccount(account);
            }
        }
    }

    /**
     * 更新当前用户的别名
     *
     * @param name
     */
    public void updateAccount(String name) {
        if (currentAccount != null) {
            // currentAccount.setName(name);
            //updateAccount(currentAccount.getAddress(),name);
            AccountsDao.getInstance().updateAccount(currentAccount, name, new AccountsDao.OnUpdateNameListener() {
                @Override
                public void onUpdateName(Account account, String name) {
                    setChanged();
                    notifyObservers();
                }
            });
        }
    }

    /**
     * 更新地址
     * @param pubKey
     * @param address
     */
    public void updateAccountAddress(String pubKey, String address) {
        for (Account account : accounts
                ) {
            if (account.getPublicKey().equals(pubKey)) {
                account.setName(address);
                AccountsDao.getInstance().updateAccount(account);
            }
        }
    }

    /**
     * 更新当前用户的地址
     *
     * @param address
     */
    public void updateAccountAddress(String address) {
        if (currentAccount != null) {
            // currentAccount.setName(name);
            //updateAccount(currentAccount.getAddress(),name);
            AccountsDao.getInstance().updateAccountAddress(currentAccount, address, new AccountsDao.OnUpdateAddressListener() {

                @Override
                public void onUpdateAddress(Account account, String address) {
                    setChanged();
                    notifyObservers();
                }
            });
        }
    }

    public void setAccountBackup(boolean isBackup) {
        if (currentAccount != null) {
            AccountsDao.getInstance().updateAccountBackup(currentAccount, isBackup, new AccountsDao.OnUpdateBackupListener() {
                @Override
                public void onUpdateBackup(Account account, boolean backup) {
                    setChanged();
                    notifyObservers();
                }
            });
        }
    }

    public void setSaveSecondPwd(@Account.States int SAVE_SECOND_STATE,String secondPwd){
        if (currentAccount!=null){
            AccountsDao.getInstance().updateSaveSecondPwd(currentAccount, SAVE_SECOND_STATE,secondPwd, new AccountsDao.OnUpdateSaveSecondPwdStateListener() {
                @Override
                public void OnUpdateSaveSecondPwdStateListener(Account account, int SAVE_SECOND_STATE) {
                    setChanged();
                    notifyObservers();
                }
            });
        }
    }

    public boolean hasAccountForName(String name) {
        return AccountsDao.getInstance().hasAccountForName(name);
    }

    public boolean hasAccountForAddress(String address) {
        return AccountsDao.getInstance().hasAccountForAddress(address);
    }

    public boolean hasAccountForSeed(String seed) {
        String publicKey=AschSDK.Helper.getPublicKey(seed);
        return AccountsDao.getInstance().hasAccountForPublicKey(publicKey);
    }

    public  rx.Observable<FullAccount> createLoadFullAccountObservable(){
        return createLoadFullAccountObservable(getCurrentAccount());
    }


    public  rx.Observable<FullAccount> createLoginAccountObservable(String publicKey){
        return rx.Observable.create(new rx.Observable.OnSubscribe<FullAccount>() {
            @Override
            public void call(Subscriber<? super FullAccount> subscriber) {
                try {
                    String address = AschFactory.getInstance().getSecurity().getAddress(publicKey);
                    AschResult result = AschSDK.Account.getAccountV2(address);
                    if (result != null && result.isSuccessful()) {
                        LogUtils.iTag(TAG, result.getRawJson());
                        FullAccount fullAccount = JSON.parseObject(result.getRawJson(), FullAccount.class);
                        if (fullAccount.getAccount()==null){
                            FullAccount.AccountInfo accountInfo = new FullAccount.AccountInfo();
                            accountInfo.setAddress(address);
                            accountInfo.setPublicKey(publicKey);
                            accountInfo.setBalance("0");
                            fullAccount.setAccount(accountInfo);
                        }
                        subscriber.onNext(fullAccount);
                        subscriber.onCompleted();
                    } else {
                        //subscriber.onNext(null);
                       // subscriber.onCompleted();
                        subscriber.onError(result.getException());
                    }
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        });
    }


    public  rx.Observable<List<Balance>> createUIAObservable(String address){
        return rx.Observable.create(new rx.Observable.OnSubscribe<List<Balance>>(){
            @Override
            public void call(Subscriber<? super List<Balance>> subscriber) {
                AschResult result = AschSDK.Account.getBalanceV2(address, 100, 0);
                LogUtils.iTag(TAG, result.getRawJson());
                if (result.isSuccessful()) {
                    JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                    JSONArray balanceJsonArray = resultJSONObj.getJSONArray("balances");
                    List<Balance> balances = JSON.parseArray(balanceJsonArray.toJSONString(), Balance.class);
                    for (Balance balance :
                            balances) {
                        //JSONObject assetJsonObj = JSONObject.parseObject(balance.getAssetJson());
                        if (balance.getFlag()==2){
                            Balance.UIAAsset uiaAsset=JSON.parseObject(balance.getAssetJson(), Balance.UIAAsset.class);
                            balance.setUiaAsset(uiaAsset);
                            balance.setType(BaseAsset.TYPE_UIA);
                        }else if (balance.getFlag()==3){
                            Balance.GatewayAsset gatewayAsset=JSON.parseObject(balance.getAssetJson(), Balance.GatewayAsset.class);
                            balance.setGatewayAsset(gatewayAsset);
                            balance.setType(BaseAsset.TYPE_GATEWAY);
                        }
                    }
                    subscriber.onNext(balances);
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                   // subscriber.onError(result.getException());
                }
            }
        });
    }



    public  rx.Observable<FullAccount> createLoadFullAccountObservable(Account account){

        ArrayList<Balance> list = new ArrayList<>();
        String publicKey = account.getPublicKey();
        String address = account.getAddress();
        rx.Observable loginObservable = createLoginAccountObservable(publicKey);
        rx.Observable uiaObservable = createUIAObservable(address);
      rx.Observable combinedObservable = rx.Observable.combineLatest(loginObservable, uiaObservable, new Func2<FullAccount, List<Balance>, FullAccount>() {
            @Override
            public FullAccount call(FullAccount fullAccount, List<Balance> balances) {
                Balance xasBalance = new Balance();
                xasBalance.setCurrency(AppConstants.XAS_NAME);
                xasBalance.setBalance(String.valueOf(fullAccount.getAccount().getBalance()));
                xasBalance.setPrecision(AppConstants.PRECISION);
                list.add(xasBalance);
                if (balances!=null){
                    list.addAll(balances);
                }
                fullAccount.setBalances(list);
                return fullAccount;
            }
        });
        return combinedObservable;
    }

    public void loadFullAccount(Account account) {
        createLoadFullAccountObservable(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
        .subscribe(new Subscriber<FullAccount>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(java.lang.Throwable e) {
                LogUtils.dTag("xasObservable error:", e.toString());
                //view.displayError(new UIException("获取余额错误"));
            }

            @Override
            public void onNext(FullAccount fullAccount) {
                LogUtils.dTag(TAG,"FullAccount info:"+fullAccount.getAccount().getAddress()+" balances:"+fullAccount.getBalances().toString());
               // account.setAddress(fullAccount.getAccount().getAddress());
                //updateAccountAddress(fullAccount.getAccount().getAddress());
                account.setFullAccount(fullAccount);
            }
        });
    }

}
