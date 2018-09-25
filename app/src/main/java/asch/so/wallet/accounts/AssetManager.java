package asch.so.wallet.accounts;

import java.util.ArrayList;

import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.AssetList;
import io.realm.Realm;

public class AssetManager {
    /**
     * 保存一个资产列表的集合，每个列表对应一个address，根据address可以查到资产list。
     */
    private static final String TAG = AssetManager.class.getSimpleName();
    private static AssetManager assetManager = null;

    ArrayList<AssetList> assetLists;
    AssetList currentAssetList;

    public static AssetManager getInstance(){
        if (assetManager==null)
            assetManager = new AssetManager();
        return assetManager;
    }

//    public AssetList getCurrentAssetList(){
//        return queryAssetList(getAccount().getAddress());
//    }

//    public void saveAssetList(AssetList list){
//        updateAssetList(list);
//    }


//    public AssetList getAssetListByAddress(String address){
//        return queryAssetList(address);
//    }

//    private void updateAssetList(AssetList list){
//        getRealm().executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.insertOrUpdate(list);
//            }
//        });
//    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }


    private Realm getRealm(){
        return Realm.getDefaultInstance();
    }

//    private AssetList queryAssetList(String addressOrPublicKey){
//        return getRealm().where(AssetList.class).equalTo("address",addressOrPublicKey).findFirst();
//    }
}
