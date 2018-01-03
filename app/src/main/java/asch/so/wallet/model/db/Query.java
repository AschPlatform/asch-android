package asch.so.wallet.model.db;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by kimziv on 2017/9/21.
 */

public abstract class Query <T extends RealmObject>{
    public abstract RealmResults<T> call(Realm realm);
}
