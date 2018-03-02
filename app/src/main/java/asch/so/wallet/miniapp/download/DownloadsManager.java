package asch.so.wallet.miniapp.download;

import android.text.TextUtils;

import com.liulishuo.filedownloader.FileDownloader;

import java.util.HashMap;

import asch.so.wallet.model.entity.Dapp;

/**
 * Created by kimziv on 2018/3/1.
 */

public class DownloadsManager {

    private HashMap<String,Downloader> downloaderHashMap;

    private static class HolderClass{
        private final static  DownloadsManager INSTANCE =new DownloadsManager();
    }

    public static DownloadsManager getImpl(){
        return HolderClass.INSTANCE;
    }

    public DownloadsManager() {
        this.downloaderHashMap = new HashMap<>();
    }

    public Downloader getDownloader(Dapp dapp){
        if (downloaderHashMap.containsKey(dapp.getTransactionId())){
            return downloaderHashMap.get(dapp.getTransactionId());
        }
        Downloader downloader = new Downloader(dapp);
        downloaderHashMap.put(dapp.getTransactionId(),downloader);
        return downloader;
    }

    public void removeDownloader(String dappId){
        if (TextUtils.isEmpty(dappId)){
            return;
        }
        downloaderHashMap.remove(dappId);
    }



    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }



}
