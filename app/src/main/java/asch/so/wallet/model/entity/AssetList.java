package asch.so.wallet.model.entity;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by kimziv on 2017/11/1.
 */

public class AssetList {

    String address;
    List<BaseAsset> assets;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<BaseAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<BaseAsset> assets) {
        this.assets = assets;
    }
}
