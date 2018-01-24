package asch.so.wallet.miniapp.download;

import android.text.TextUtils;
import android.util.SparseArray;

import com.chad.library.adapter.base.BaseViewHolder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import asch.so.wallet.model.entity.Dapp;

/**
 * Created by kimziv on 2018/1/22.
 */

public class TasksManager {
    private final static class HolderClass {
        private final static TasksManager INSTANCE
                = new TasksManager();
    }

    public static TasksManager getImpl() {
        return HolderClass.INSTANCE;
    }

    private TasksDBContraller dbController;
    private ArrayList<TaskModel> modelList;

    private TasksManager() {
        dbController = new TasksDBContraller();
        modelList = dbController.getAllTasks();

       // initDemo();
    }

//    private void initDemo() {
//        if (modelList.size() <= 0) {
//            final int demoSize = Constant.BIG_FILE_URLS.length;
//            for (int i = 0; i < demoSize; i++) {
//                final String url = Constant.BIG_FILE_URLS[i];
//                addTask(url);
//            }
//        }
//    }

    private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

    public void addTaskForViewHolder(final BaseDownloadTask task) {
        taskSparseArray.put(task.getId(), task);
    }

    public void removeTaskForViewHolder(final int id) {
        taskSparseArray.remove(id);
    }

    public void updateViewHolder(final int id, final BaseViewHolder holder) {
        final BaseDownloadTask task = taskSparseArray.get(id);
        if (task == null) {
            return;
        }

        task.setTag(holder);
    }

    public void releaseTask() {
        taskSparseArray.clear();
    }

    private FileDownloadConnectListener listener;

    private void registerServiceConnectionListener(final WeakReference<ServiceConnectionListener>
                                                           activityWeakReference) {
        if (listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(listener);
        }

        listener = new FileDownloadConnectListener() {

            @Override
            public void connected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }

            @Override
            public void disconnected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }
        };

        FileDownloader.getImpl().addServiceConnectListener(listener);
    }

    private void unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
    }

    public void onCreate(final WeakReference<ServiceConnectionListener> activityWeakReference) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            registerServiceConnectionListener(activityWeakReference);
        }
    }

    public void onDestroy() {
        unregisterServiceConnectionListener();
        releaseTask();
    }

    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    public TaskModel get(final int position) {
        return modelList.get(position);
    }

    public TaskModel getById(final int id) {
        for (TaskModel model : modelList) {
            if (model.getId() == id) {
                return model;
            }
        }

        return null;
    }

    public TaskModel getTaskByDappId(String dappId){
        if (dappId==null){
            return null;
        }
        for (TaskModel model:modelList){
            if (dappId.equals(model.getDapp().getTransactionId())){
                return model;
            }
        }
        return null;
    }

    /**
     * @param status Download Status
     * @return has already downloaded
     * @see FileDownloadStatus
     */
    public boolean isDownloaded(final int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(final int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotal(final int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFar(final int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }

    public int getTaskCounts() {
        return modelList.size();
    }

    public TaskModel addTask(final Dapp dapp) {
        return addTask(dapp, createPath(dapp.getLink()));
    }

    public TaskModel addTask(final Dapp dapp, final String path) {
        if (dapp==null || TextUtils.isEmpty(dapp.getLink()) || TextUtils.isEmpty(path)) {
            return null;
        }

        final int id = FileDownloadUtils.generateId(dapp.getLink(), path);
        TaskModel model = getById(id);
        if (model != null) {
            return model;
        }
        final TaskModel newModel = dbController.addTask(dapp, path);
        if (newModel != null) {
            modelList.add(newModel);
        }

        return newModel;
    }

    public String createPath(final String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        return FileDownloadUtils.getDefaultSaveFilePath(url);
    }


    public interface ServiceConnectionListener{
         void postNotifyDataChanged();
    }
}
