package asch.io.wallet.miniapp.download;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import asch.io.wallet.event.DAppDownloadEvent;
import asch.io.wallet.miniapp.unzip.UnZip;
import asch.io.wallet.model.entity.DApp;

/**
 * Created by kimziv on 2018/2/28.
 */

public class Downloader {

    private String downloadRootDir;
    private DApp dapp;
    private BaseDownloadTask downloadTask;
    private FileDownloadSampleListener downloadListener;
    private int downloadId;
    private String downloadUrl;
    private String downloadPath;
    private String installedPath;

    /**
     * 构造函数
     * @param dapp
     */
    public Downloader(DApp dapp) {
        downloadRootDir=  FileDownloadHelper.getAppContext().getExternalFilesDir(null).getAbsolutePath() + File.separator + "asch_dapps";
        DApp dbDapp=DownloadsDB.getImpl().queryDApp(dapp.getTransactionId());
        if (dbDapp!=null){
            this.dapp = dbDapp;
        }else {
            this.dapp=dapp;
        }

        this.downloadUrl=dapp.getLink();
        this.downloadPath= getPath(dapp.getTransactionId());
        this.installedPath=getInstalledPath(dapp.getTransactionId());
        this.downloadId=FileDownloadUtils.generateId(this.downloadUrl, this.downloadPath);


        this.downloadListener=new FileDownloadSampleListener(){
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.pending(task, soFarBytes, totalBytes);
                postEvent(task.getId(), FileDownloadStatus.pending,soFarBytes,totalBytes);
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.progress(task, soFarBytes, totalBytes);
                postEvent(task.getId(),FileDownloadStatus.progress,soFarBytes,totalBytes);
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
                super.blockComplete(task);
                postEvent(task.getId(),FileDownloadStatus.blockComplete,0,0);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                DownloadsDB.getImpl().updateStatus(dapp,FileDownloadStatus.completed,null);
                postEvent(task.getId(),FileDownloadStatus.completed,0,0);
                //接着安装
                install();
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.paused(task, soFarBytes, totalBytes);
                postEvent(task.getId(),FileDownloadStatus.paused,soFarBytes,totalBytes);

                DownloadsDB.getImpl().updateStatus(dapp,FileDownloadStatus.paused,soFarBytes,totalBytes,null);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                super.error(task, e);
                postEvent(task.getId(),FileDownloadStatus.error,0,0);
                DownloadsDB.getImpl().updateStatus(dapp,FileDownloadStatus.error,null);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                super.warn(task);
                postEvent(task.getId(),FileDownloadStatus.warn,0,0);
            }

            @Override
            protected void started(BaseDownloadTask task) {
                super.started(task);
                postEvent(task.getId(),FileDownloadStatus.started,0,0);
                dapp.setDownloadId(getDownloadId());
                dapp.setDownloadPath(getDownloadPath());
                dapp.setInstalledPath(getInstalledPath());
//                dapp.setStatus(FileDownloadStatus.started);
                DownloadsDB.getImpl().updateStatus(dapp,FileDownloadStatus.started,null);
            }
        };
    }

    protected void unzipFile( File zipFile, File destination ) {
        UnZip decomp = new UnZip( zipFile.getPath(),
                destination.getPath() + File.separator );
        decomp.unzip();
    }


    private void installedApp(Downloader downloader){
        unzipFile( new File(downloader.getDownloadPath()), new File(downloader.getInstalledPath()));
        DownloadsDB.getImpl().updateStatus(dapp,DownloadExtraStatus.INSTALLED,null);
        postEvent(getDownloadId(),DownloadExtraStatus.INSTALLED,0,0);
    }

    private void uninstallApp(Downloader downloader){
        boolean ret= FileUtils.deleteDir(downloader.getInstalledPath());
        boolean ret2=FileUtils.deleteFile(downloader.getDownloadPath());
        if (ret && ret2){
            DownloadsDB.getImpl().updateStatus(dapp,DownloadExtraStatus.UNINSTALLED,null);
            //DownloadsDB.getImpl().deleteDApp(dapp.getTransactionId(),null);
            postEvent(getDownloadId(),DownloadExtraStatus.UNINSTALLED,0,0);

        }
    }

    /**
     * getEvetnBus
     * @return
     */
    private EventBus getEvetnBus(){
        return EventBus.getDefault();
    }

    /**
     * 注册订阅者
     * @param subscriber
     */
    public void registerSubscriber(Object subscriber){
        getEvetnBus().register(subscriber);
    }

    /**
     * 移除订阅者
     * @param subscriber
     */
    public void unregisterSubscriber(Object subscriber){
        getEvetnBus().unregister(subscriber);
    }

    /**
     * 通知订阅者
     * @param event
     */
    private void postEvent(DAppDownloadEvent event){
        getEvetnBus().post(event);
    }

    /**
     * 通知订阅者
     * @param status
     * @param soFarBytes
     * @param totalBytes
     */
    private void postEvent(int taskId, int status, long soFarBytes, long totalBytes){

        postEvent(new DAppDownloadEvent(taskId,status,soFarBytes,totalBytes));
    }

    public BaseDownloadTask getDownloadTask() {
        return downloadTask;
    }

    public void setDownloadTask(BaseDownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    /**
     * 开始下载
     */
    public void start(){
        String path=getDownloadPath(); //getPath(DApp.getTransactionId());
        if (dapp ==null || TextUtils.isEmpty(dapp.getLink()) || TextUtils.isEmpty(path)) {
            return;
        }
        //final int id = FileDownloadUtils.generateId(dapp.getLink(), path);
          downloadTask = createDownloadTask(dapp,downloadListener);
          downloadTask.start();
          DownloadsDB.getImpl().updateStatus(dapp,FileDownloadStatus.progress,null);
    }

    /**
     * 暂停下载
     */
    public void pause(){
        if (downloadTask!=null){
            downloadTask.pause();
//            dapp.setStatus(FileDownloadStatus.paused);
//            DownloadsDB.getImpl().updateStatus(dapp.getTransactionId(),FileDownloadStatus.paused,null);
        }
    }

    /**
     * 恢复下载
     */
    public void resume(){
        //if (downloadTask==null) {
            downloadTask = createDownloadTask(dapp,downloadListener);
        //}
        downloadTask.start();
        DownloadsDB.getImpl().updateStatus(dapp,FileDownloadStatus.progress,null);

    }

    /**
     * 取消下载
     */
    public void cancel(){
        if (downloadTask!=null){
            downloadTask.cancel();
            DownloadsDB.getImpl().updateStatus(dapp,FileDownloadStatus.INVALID_STATUS,null);
        }
    }

    public void install(){
        //if (downloadTask!=null){
            installedApp(this);
            //dapp.setStatus(DownloadExtraStatus.INSTALLED);
//            DownloadsDB.getImpl().updateStatus(dapp.getTransactionId(),DownloadExtraStatus.INSTALLED,null);
       // }
    }

    public void uninstall(){
        //if (downloadTask!=null){
            uninstallApp(this);
            //dapp.setStatus(DownloadExtraStatus.UNINSTALLED);

       // }
    }

    /**
     * 创建一个下载任务
     * @param DApp
     * @param downloadListener
     * @return
     */
    private BaseDownloadTask createDownloadTask(DApp DApp, FileDownloadSampleListener downloadListener) {
        final String url= DApp.getLink();
        boolean isDir = false;
        String path = getDownloadPath(); //getPath(DApp.getTransactionId());
        return FileDownloader.getImpl().create(url)
                .setPath(path, isDir)
                .setMinIntervalUpdateSpeed(50)
                //.setTag(holder)
                .setListener(downloadListener);
    }

    /**
     * 获取下载状态
     * @return
     */
    public int getStatus() {
         String path=getDownloadPath();// getPath(DApp.getTransactionId());
        int taskId=getTaskId(dapp.getLink(),path);
        return FileDownloader.getImpl().getStatus(taskId, path);
    }

    /**
     * 获取文件字节数
     * @return
     */
    public long getTotal() {
//        String path=getDownloadPath();// getPath(DApp.getTransactionId());
//        int taskId=getTaskId(dapp.getLink(),path);
//        return FileDownloader.getImpl().getTotal(taskId);
        return dapp.getTotalBytes();
    }

    /**
     * 获取当前下载字节数
     * @return
     */
    public long getSoFar() {
        //String path=getDownloadPath();// getPath(DApp.getTransactionId());
        //int taskId=getTaskId(dapp.getLink(),path);
//        return FileDownloader.getImpl().getSoFar(taskId);
        return dapp.getSofarBytes();
    }

    /**
     * 根据dapp id 或者下载路径
     * @param dappId
     * @return
     */
    private String getPath(String dappId){
        return downloadRootDir+File.separator+dappId+".zip";
    }

//    public String getPath(){
//        return DOWNLOAD_ROOT_PATH+File.separator+ DApp.getTransactionId()+".zip";
//    }

    /**
     * dapp安装路径
     * @param dappId
     * @return
     */
    private String getInstalledPath(String dappId){
        return downloadRootDir+File.separator+dappId;
    }

//    public String getInstalledPath(){
//        return DOWNLOAD_ROOT_PATH+File.separator+ DApp.getTransactionId();
//    }


    /**
     * 映射下载id
     * @param url
     * @param path
     * @return
     */
    private int getTaskId(String url, String path){
        return FileDownloadUtils.generateId(url, path);
    }

//    public final static class HolderClass{
//        private final static Downloader INSTANCE = new Downloader();
//    }


    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getInstalledPath() {
        return installedPath;
    }

    public void setInstalledPath(String installedPath) {
        this.installedPath = installedPath;
    }

    public DApp getDapp() {
        return dapp;
    }

    public void setDapp(DApp dapp) {
        this.dapp = dapp;
    }
}
