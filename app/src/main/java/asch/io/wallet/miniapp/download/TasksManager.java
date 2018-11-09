package asch.io.wallet.miniapp.download;

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
//
//    private TasksDBContraller dbController;
//    private ArrayList<TaskModel> modelList;
//
//    private TasksManager() {
//        dbController = new TasksDBContraller();
//        modelList = dbController.getAllTasks();
//    }
//
//    private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();
//
//    public void addTaskForViewHolder(final BaseDownloadTask task) {
//        taskSparseArray.put(task.getId(), task);
//    }
//
//    public void removeTaskForViewHolder(final int id) {
//        taskSparseArray.remove(id);
//    }
//
//    public void updateViewHolder(final int id, final BaseViewHolder holder) {
//        final BaseDownloadTask task = taskSparseArray.get(id);
//        if (task == null) {
//            return;
//        }
//
//        task.setTag(holder);
//    }
//
//    public void releaseTask() {
//        taskSparseArray.clear();
//    }
//
//    private FileDownloadConnectListener listener;
//
//    private void registerServiceConnectionListener(final WeakReference<ServiceConnectionListener>
//                                                           activityWeakReference) {
//        if (listener != null) {
//            FileDownloader.getImpl().removeServiceConnectListener(listener);
//        }
//
//        listener = new FileDownloadConnectListener() {
//
//            @Override
//            public void connected() {
//                if (activityWeakReference == null
//                        || activityWeakReference.get() == null) {
//                    return;
//                }
//
//                activityWeakReference.get().postNotifyDataChanged();
//            }
//
//            @Override
//            public void disconnected() {
//                if (activityWeakReference == null
//                        || activityWeakReference.get() == null) {
//                    return;
//                }
//
//                activityWeakReference.get().postNotifyDataChanged();
//            }
//        };
//
//        FileDownloader.getImpl().addServiceConnectListener(listener);
//    }
//
//    private void unregisterServiceConnectionListener() {
//        FileDownloader.getImpl().removeServiceConnectListener(listener);
//        listener = null;
//    }
//
//    public void onCreate(final WeakReference<ServiceConnectionListener> activityWeakReference) {
//        if (!FileDownloader.getImpl().isServiceConnected()) {
//            FileDownloader.getImpl().bindService();
//            registerServiceConnectionListener(activityWeakReference);
//        }
//    }
//
//    public void onDestroy() {
//        unregisterServiceConnectionListener();
//        releaseTask();
//    }
//
//    public boolean isReady() {
//        return FileDownloader.getImpl().isServiceConnected();
//    }
//
//    public TaskModel get(final int position) {
//        return modelList.get(position);
//    }
//
//    public TaskModel getById(final int id) {
//        for (TaskModel model : modelList) {
//            if (model.getId() == id) {
//                return model;
//            }
//        }
//
//        return null;
//    }
//
//    public TaskModel getTaskByDappId(String dappId){
//        if (dappId==null){
//            return null;
//        }
//        for (TaskModel model:modelList){
//            if (dappId.equals(model.getDApp().getTransactionId())){
//                return model;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * @param status Download Status
//     * @return has already downloaded
//     * @see FileDownloadStatus
//     */
//    public boolean isDownloaded(final int status) {
//        return status == FileDownloadStatus.completed;
//    }
//
//    public int getStatus(final int id, String path) {
//        return FileDownloader.getImpl().getStatus(id, path);
//    }
//
//    public long getTotal(final int id) {
//        return FileDownloader.getImpl().getTotal(id);
//    }
//
//    public long getSoFar(final int id) {
//        return FileDownloader.getImpl().getSoFar(id);
//    }
//
//    public int getTaskCounts() {
//        return modelList.size();
//    }
//
//    public TaskModel addTask(final DApp DApp) {
//        return addTask(DApp, createPath(DApp.getLink()));
//    }
//
//    public TaskModel addTask(final DApp DApp, final String path) {
//        if (DApp ==null || TextUtils.isEmpty(DApp.getLink()) || TextUtils.isEmpty(path)) {
//            return null;
//        }
//
//        final int id = FileDownloadUtils.generateId(DApp.getLink(), path);
//        TaskModel model = getById(id);
//        if (model != null) {
//            return model;
//        }
//        final TaskModel newModel = dbController.addTask(DApp, path);
//        if (newModel != null) {
//            modelList.add(newModel);
//        }
//
//        return newModel;
//    }
//
//    public String createPath(final String url) {
//        if (TextUtils.isEmpty(url)) {
//            return null;
//        }
//
//        return FileDownloadUtils.getDefaultSaveFilePath(url);
//    }
//
//
//    public interface ServiceConnectionListener{
//         void postNotifyDataChanged();
//    }
}
