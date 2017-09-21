package asch.so.wallet.model.entity;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kimziv on 2017/9/21.
 */

public class Account extends RealmObject{

    public String name;
    @PrimaryKey
    public String address;
    public String publicKey;

}
