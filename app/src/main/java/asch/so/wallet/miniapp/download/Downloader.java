package asch.so.wallet.miniapp.download;

import android.text.TextUtils;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Observable;

import asch.so.wallet.event.DAppDownloadEvent;
import asch.so.wallet.model.entity.Dapp;

/**
 * Created by kimziv on 2018/2/28.
 */

public class Downloader {

    private static final  String DOWNLOAD_ROOT_PATH = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "asch_dapps";
    private Dapp dapp;
    private BaseDownloadTask downloadTask;
    private FileDownloadSampleListener downloadListener;


    /**
     * 构造函数
     * @param dapp
     */
    public Downloader(Dapp dapp) {
        this.dapp=dapp;
        this.downloadListener=new FileDownloadSampleListener(){
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.pending(task, soFarBytes, totalBytes);
                postEvent(FileDownloadStatus.pending,soFarBytes,totalBytes);
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.progress(task, soFarBytes, totalBytes);
                postEvent(FileDownloadStatus.progress,soFarBytes,totalBytes);
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
                super.blockComplete(task);
                postEvent(FileDownloadStatus.blockComplete,0,0);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                postEvent(FileDownloadStatus.completed,0,0);
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.paused(task, soFarBytes, totalBytes);
                postEvent(FileDownloadStatus.paused,soFarBytes,totalBytes);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                super.error(task, e);
                postEvent(FileDownloadStatus.error,0,0);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                super.warn(task);
                postEvent(FileDownloadStatus.warn,0,0);
            }

            @Override
            protected void started(BaseDownloadTask task) {
                super.started(task);
                postEvent(FileDownloadStatus.started,0,0);
            }
        };
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
    private void postEvent(int status, int soFarBytes, int totalBytes){

        postEvent(new DAppDownloadEvent(status,soFarBytes,totalBytes));
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
        String path=getPath(dapp.getTransactionId());
        if (dapp==null || TextUtils.isEmpty(dapp.getLink()) || TextUtils.isEmpty(path)) {
            return;
        }
        //final int id = FileDownloadUtils.generateId(dapp.getLink(), path);
          downloadTask = createDownloadTask(dapp,downloadListener);
          downloadTask.start();
    }

    /**
     * 暂停下载
     */
    public void pause(){
        if (downloadTask!=null){
            downloadTask.pause();
        }
    }

    /**
     * 恢复下载
     */
    public void resume(){
        downloadTask = createDownloadTask(dapp,downloadListener);
        downloadTask.start();
    }

    /**
     * 取消下载
     */
    public void cancel(){
        if (downloadTask!=null){
            downloadTask.cancel();
        }
    }

    /**
     * 创建一个下载任务
     * @param dapp
     * @param downloadListener
     * @return
     */
    private BaseDownloadTask createDownloadTask(Dapp dapp, FileDownloadSampleListener downloadListener) {
        final String url= dapp.getLink();
        boolean isDir = false;
        String path =getPath(dapp.getTransactionId());
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
         String path=getPath(dapp.getTransactionId());
        int taskId=getTaskId(dapp.getLink(),path);
        return FileDownloader.getImpl().getStatus(taskId, path);
    }

    /**
     * 获取文件字节数
     * @return
     */
    public long getTotal() {
        String path=getPath(dapp.getTransactionId());
        int taskId=getTaskId(dapp.getLink(),path);
        return FileDownloader.getImpl().getTotal(taskId);
    }

    /**
     * 获取当前下载字节数
     * @return
     */
    public long getSoFar() {
        String path=getPath(dapp.getTransactionId());
        int taskId=getTaskId(dapp.getLink(),path);
        return FileDownloader.getImpl().getSoFar(taskId);
    }

    /**
     * 根据dapp id 或者下载路径
     * @param dappId
     * @return
     */
    private String getPath(String dappId){
        return DOWNLOAD_ROOT_PATH+File.separator+dappId+".zip";
    }

    public String getPath(){
        return DOWNLOAD_ROOT_PATH+File.separator+dapp.getTransactionId()+".zip";
    }

    /**
     * dapp安装路径
     * @param dappId
     * @return
     */
    private String getInstalledPath(String dappId){
        return DOWNLOAD_ROOT_PATH+File.separator+dappId;
    }

    public String getInstalledPath(){
        return DOWNLOAD_ROOT_PATH+File.separator+dapp.getTransactionId();
    }


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
}
