package asch.so.wallet.model.db.dao;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import asch.so.wallet.model.entity.Account;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
     *
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
     *
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
                account.setName(name);
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
                account.setBackup(isBackup);
                if (listener!=null){
                    listener.onUpdateBackup(account,isBackup);
                }
            }
        });
    }

    public void  updateAccountAddress(Account account, String address, OnUpdateAddressListener listener){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                account.setAddress(address);
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

    public interface OnUpdateAddressListener{

        void onUpdateAddress(Account account, String address);
    }

}
