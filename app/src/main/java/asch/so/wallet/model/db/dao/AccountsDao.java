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

    public AccountsDao(){

    }

    @Inject
    AccountsDao(Context context){
        this.context=context;
    }

    private Realm getRealm(){
        return Realm.getDefaultInstance();
    }


    /**
     *
     * @return
     */
   public RealmResults<Account> queryAllSavedAccounts(){

    RealmResults<Account> results = getRealm().where(Account.class).findAllAsync();
    //Log.v(TAG,"+++++++"+results.get(0).getName());
    return results;
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
   public void  updateAccount(Account account){

   }

    /**
     *
     * @param name
     */
    public void  updateAccountName(String name){

    }



}
