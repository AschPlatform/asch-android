package asch.so.wallet.model.db;

import io.realm.Realm;

/**
 * Created by kimziv on 2017/9/21.
 */

public abstract class Exec {

    public abstract void run(Realm realm);
}
