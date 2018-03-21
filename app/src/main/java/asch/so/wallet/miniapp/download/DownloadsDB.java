package asch.so.wallet.miniapp.download;

import android.text.TextUtils;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.ArrayList;

import asch.so.wallet.model.entity.DApp;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.http.PUT;

/**
 * Created by kimziv on 2018/3/5.
 */

public class DownloadsDB {

    public static DownloadsDB getImpl(){
        return HolderClass.INSTANCE;
    }


    public ArrayList<DApp> queryAllDApps() {

        RealmResults<DApp> results = getRealm().where(DApp.class).findAll();
        ArrayList<DApp> dApps= new ArrayList<>(results);
        return dApps;
    }

    public ArrayList<DApp> queryInstalledDApps() {

        RealmResults<DApp> results = getRealm().where(DApp.class).equalTo("status",DownloadExtraStatus.INSTALLED).findAll();
        ArrayList<DApp> dApps= new ArrayList<>(results);
        return dApps;
    }

    /**
     * 查询
     * @param dappId
     * @return
     */
    public DApp queryDApp(String dappId) {

        DApp dapp = getRealm().where(DApp.class).equalTo("transactionId",dappId).findFirst();
        return dapp;
    }

    /**
     * 增加
     * @param dapp
     * @param listener
     * @return
     */
    public DApp addDApp(DApp dapp, OnAddDAppListener listener){
        if ( dapp ==null  || TextUtils.isEmpty(dapp.getInstalledPath())) {
            if (listener!=null){
                listener.onAddDApp(dapp);
            }
            return null;
        }
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(dapp);
                if (listener!=null){
                    listener.onAddDApp(dapp);
                }
            }
        });
        return dapp;

    }

    /**
     * 删除
     * @param dappId
     * @param listener
     */
    public void deleteDApp(String dappId, OnDeleteDAppListener listener){

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               DApp dapp=queryDApp(dappId);
                if (dapp!=null){
                    dapp.deleteFromRealm();
                }
                if (listener!=null){
                    listener.onDeleteDApp(dapp);
                }
            }
        });
    }

    /**
     * 更新dapp
     * @param dapp
     * @param listener
     * @return
     */
    public DApp updateDApp(DApp dapp, OnUpdateDAppListener listener){
        if ( dapp ==null  || TextUtils.isEmpty(dapp.getInstalledPath())) {
            if (listener!=null){
                listener.onUpdateDApp(dapp);
            }
            return null;
        }
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(dapp);
                if (listener!=null){
                    listener.onUpdateDApp(dapp);
                }
            }
        });
        return dapp;
    }

    public DApp updateStatus(DApp dapp, int status, OnUpdateDAppListener listener){
        if ( dapp ==null) {
            if (listener!=null){
                listener.onUpdateDApp(dapp);
            }
            return null;
        }
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dapp.setStatus(status);
                realm.insertOrUpdate(dapp);
                if (listener!=null){
                    listener.onUpdateDApp(dapp);
                }
            }
        });
        return dapp;
    }

    public DApp updateStatus(DApp dapp, int status, long sofarBytes, long totalBytes, OnUpdateDAppListener listener){
        if ( dapp ==null) {
            if (listener!=null){
                listener.onUpdateDApp(dapp);
            }
            return null;
        }
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dapp.setStatus(status);
                dapp.setSofarBytes(sofarBytes);
                dapp.setTotalBytes(totalBytes);
                realm.insertOrUpdate(dapp);
                if (listener!=null){
                    listener.onUpdateDApp(dapp);
                }
            }
        });
        return dapp;
    }

    /**
     *
     * @param dappId
     * @param status
     * @param listener
     */
    public void updateStatus(String dappId, int status, OnUpdateDAppListener listener){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DApp dapp=queryDApp(dappId);
                dapp.setStatus(status);
                realm.insertOrUpdate(dapp);
                if (listener!=null){
                    listener.onUpdateDApp(dapp);
                }
            }
        });
    }

    /**
     *
     * @param dappId
     * @param downloadId
     * @param listener
     */
    public void updateDownloadPath(String dappId, int downloadId, OnUpdateDAppListener listener){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DApp dapp=queryDApp(dappId);
                dapp.setDownloadId(downloadId);
                realm.insertOrUpdate(dapp);
                if (listener!=null){
                    listener.onUpdateDApp(dapp);
                }
            }
        });
    }

    /**
     *
     * @param dappId
     * @param path
     * @param listener
     */
    public void updateDownloadPath(String dappId, String path, OnUpdateDAppListener listener){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DApp dapp=queryDApp(dappId);
                dapp.setDownloadPath(path);
                realm.insertOrUpdate(dapp);
                if (listener!=null){
                    listener.onUpdateDApp(dapp);
                }
            }
        });
    }

    /**
     *
     * @param dappId
     * @param path
     * @param listener
     */
    public void updateInstalledPath(String dappId, String path, OnUpdateDAppListener listener){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DApp dapp=queryDApp(dappId);
                dapp.setInstalledPath(path);
                realm.insertOrUpdate(dapp);
                if (listener!=null){
                    listener.onUpdateDApp(dapp);
                }
            }
        });
    }

    private Realm getRealm(){
        return Realm.getDefaultInstance();
    }





    public interface OnDeleteDAppListener{
        void onDeleteDApp(DApp dapp);
    }

    public interface OnAddDAppListener{
        void onAddDApp(DApp dapp);
    }

    public interface OnUpdateDAppListener{
        void onUpdateDApp(DApp dapp);
    }

    private static class HolderClass{
        private final static DownloadsDB INSTANCE=new DownloadsDB();
    }
}
