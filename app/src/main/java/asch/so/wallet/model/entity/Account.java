package asch.so.wallet.model.entity;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kimziv on 2017/9/21.
 */

public class Account extends RealmObject{

    //钱包别名
    private String name;
    //@PrimaryKey
    //地址
    private String address;
    //公钥
    private String publicKey;



    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
