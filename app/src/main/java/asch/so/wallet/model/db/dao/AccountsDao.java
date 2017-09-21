package asch.so.wallet.model.db.dao;

import android.content.Context;

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

    private Context context;

    @Inject
    AccountsDao(Context context){
        this.context=context;
    }

    private Realm getRealm(){
        return Realm.getDefaultInstance();
    }


    public Observable<RealmResults<Account>> queryAllSavedAccounts(){

        return getRealm().where(Account.class).findAllAsync().asObservable();
    }
}
