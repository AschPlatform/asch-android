package asch.io.wallet.model.db.dao;

import android.content.Context;

import javax.inject.Inject;

import asch.io.wallet.model.entity.Account;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountsDao {

    private static final String TAG=AccountsDao.class.getSimpleName();

    private Context context;
    private  static  AccountsDao accountsDao=null;

    public AccountsDao(){

    }

    @Inject
    AccountsDao(Context context){
        this.context=context;
    }

    public static AccountsDao getInstance(){
        if (accountsDao==null){
            accountsDao=new AccountsDao();
        }
        return accountsDao;
    }

    private Realm getRealm(){

        return Realm.getDefaultInstance();
    }


    /**
     *
     * @return
     */
   public RealmResults<Account> queryAllSavedAccounts(){

    RealmResults<Account> results = getRealm().where(Account.class).findAll();
       
    return results;
   }

   public Account queryAccount(String addressOrPublicKey){

     return getRealm().where(Account.class).equalTo("address",addressOrPublicKey).findFirst();

   }

//    public boolean hasAccountBackup{
//        return getRealm().where(Account.class).equalTo("backup",true).count()>0;
//    }

   public boolean hasAccountForName(String name){
       return getRealm().where(Account.class).equalTo("name",name).count()>0;
   }

    public boolean hasAccountForAddress(String address){
        return getRealm().where(Account.class).equalTo("address",address).count()>0;
    }

    public boolean hasAccountForPublicKey(String pubkey){
        return getRealm().where(Account.class).equalTo("publicKey",pubkey).count()>0;
    }

    /**
     * 获取当前账户
     * @return
     */
   public Account queryCurrentAccount(){
       return getRealm().where(Account.class).findFirst();
   }

    /**
     * @param account
     */
   public void addAccount(Account account){
       getRealm().executeTransaction(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
              realm.insertOrUpdate(account);
           }
       });

   }

    /**
     *
     * @param address
     */
   public void  removeAccount(String address){
       getRealm().executeTransaction(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {

           }
       });
   }

    /**
     *
     * @param account
     */
    public void  removeAccount(Account account){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                    account.deleteFromRealm();
            }
        });
    }

    /**
     * @param account
     */
   public void  updateAccount(Account account){
       getRealm().executeTransaction(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
               realm.insertOrUpdate(account);
           }
       });
   }

    public void  updateAccount(Account account, String name){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                account.setName(name);
//                realm.insertOrUpdate(account);
            }
        });
    }

    public void  updateAccount(Account account, String name, OnUpdateNameListener listener){

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Account dbAccount=queryAccount(account.getAddress());
                account.setName(name);
                dbAccount.setName(name);
                if (listener!=null){
                    listener.onUpdateName(account,name);
                }
            }
        });
    }

    public void  updateAccountBackup(Account account, boolean isBackup, OnUpdateBackupListener listener){

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Account dbAccount=queryAccount(account.getAddress());
                account.setBackup(isBackup);
                dbAccount.setBackup(isBackup);
                if (listener!=null){
                    listener.onUpdateBackup(account,isBackup);
                }
            }
        });
    }

    public void  updateSaveSecondPwd(Account account, @Account.States int STATE_SAVE_SECOND, String password,String secondPwd, OnUpdateSaveSecondPwdStateListener listener){

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Account dbAccount=queryAccount(account.getAddress());
                account.setSaveSecondPasswordState(STATE_SAVE_SECOND);
                account.setSecondSecret(password,secondPwd);
                dbAccount.setSaveSecondPasswordState(STATE_SAVE_SECOND);
                dbAccount.setSecondSecret(password,secondPwd);
                if (listener!=null){
                    listener.OnUpdateSaveSecondPwdStateListener(account,STATE_SAVE_SECOND);
                }
            }
        });
    }

    public void  updateAccountAddress(Account account, String address, OnUpdateAddressListener listener){

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Account dbAccount=queryAccount(account.getAddress());
                account.setAddress(address);
                dbAccount.setAddress(address);
                if (listener!=null){
                    listener.onUpdateAddress(account,address);
                }
            }
        });
    }

    public interface OnUpdateNameListener{

        void onUpdateName(Account account, String name);
    }

    public interface OnUpdateBackupListener{

        void onUpdateBackup(Account account, boolean backup);
    }

    public interface OnUpdateSaveSecondPwdStateListener{

        void OnUpdateSaveSecondPwdStateListener(Account account,  @Account.States int SAVE_SECOND_STATE);
    }

    public interface OnUpdateAddressListener{

        void onUpdateAddress(Account account, String address);
    }

}
