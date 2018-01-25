package asch.so.wallet.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.DownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import asch.so.wallet.R;
import asch.so.wallet.activity.BaseCordovaActivity;
import asch.so.wallet.event.DAppChangeEvent;
import asch.so.wallet.miniapp.download.TaskModel;
import asch.so.wallet.miniapp.download.TasksDBContraller;
import asch.so.wallet.miniapp.download.TasksManager;
import asch.so.wallet.miniapp.unzip.UnZip;
import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.util.AppUtil;
import asch.so.widget.downloadbutton.DownloadProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2018/1/19.
 */

public class DAppsAdapter extends BaseQuickAdapter<Dapp, DAppsAdapter.ViewHolder> {
    private static final String TAG=DAppsAdapter.class.getSimpleName();
    private Context context;
    private HashMap<String,TaskModel> tasksMap;
    private static final  String DOWNLOAD_PATH = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tmpdir1";
    private BaseDownloadTask downloadTask;

    public DAppsAdapter(Context context) {
        super(R.layout.item_dapps);
        this.context = context;
        tasksMap=new HashMap<>();
    }

    @Override
    protected void convert(ViewHolder holder, Dapp item) {
        holder.nameTv.setText(item.getName());
        holder.descriptionTv.setText(item.getName());
        holder.downloadBtn.setCurrentText(mContext.getString(R.string.download));
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleState(holder,item);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TasksDBContraller.getImpl().deleteTaskByDappId(item.getTransactionId(), new TasksDBContraller.OnDeleteTaskListener() {
                    @Override
                    public void onDeleteTask(TaskModel taskModel) {
                        boolean ret= FileUtils.deleteDir(new File(DOWNLOAD_PATH));
                        if (ret){
                            EventBus.getDefault().post(new DAppChangeEvent());
                        }
                        AppUtil.toastInfo(mContext,ret?"删除成功":"删除失败");

                        holder.downloadBtn.setMaxProgress(1);
                        holder.downloadBtn.setProgress(0);
                        holder.downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
                        holder.downloadBtn.setCurrentText(mContext.getString(R.string.download));
                    }
                });

            }
        });

//        if (TasksManager.getImpl().isReady()) {
//            if (tasksMap.containsKey(item.getTransactionId())){
//
//            TaskModel model=tasksMap.get(item.getTransactionId());
//            final int status = TasksManager.getImpl().getStatus(model.getId(), model.getPath());
//            if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
//                    status == FileDownloadStatus.connected) {
//                // start task, but file not created yet
//                holder.updateDownloading(status, TasksManager.getImpl().getSoFar(model.getId())
//                        , TasksManager.getImpl().getTotal(model.getId()));
//            } else if (!new File(model.getPath()).exists() &&
//                    !new File(FileDownloadUtils.getTempPath(model.getPath())).exists()) {
//                // not exist file
//                holder.updateNotDownloaded(status, 0, 0);
//            } else if (TasksManager.getImpl().isDownloaded(status)) {
//                // already downloaded and exist
//                holder.updateDownloaded();
//            } else if (status == FileDownloadStatus.progress) {
//                // downloading
//                holder.updateDownloading(status, TasksManager.getImpl().getSoFar(model.getId())
//                        , TasksManager.getImpl().getTotal(model.getId()));
//            } else {
//
//                // not start
//                holder.updateNotDownloaded(status, TasksManager.getImpl().getSoFar(model.getId())
//                        , TasksManager.getImpl().getTotal(model.getId()));
//            }
//
//            }else{
//
//            }
//        } else {
////            holder.taskStatusTv.setText(R.string.tasks_manager_demo_status_loading);
//            holder.downloadBtn.setText(R.string.tasks_manager_demo_status_loading);
//            //holder.downloadBtn.setEnabled(false);
//        }
    }


    private BaseDownloadTask createDownloadTask(ViewHolder holder, Dapp item) {
        final String url="http://asch-public.oss-cn-beijing.aliyuncs.com/appupdate/test/www.zip";
        boolean isDir = true;
        String path = DOWNLOAD_PATH;

        return FileDownloader.getImpl().create(url)
                .setPath(path, isDir)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setTag(holder)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                       // ((ViewHolder) task.getTag()).updatePending(task);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        final float percent = soFarBytes / (float) totalBytes;
                        holder.downloadBtn.setState(DownloadProgressButton.STATE_DOWNLOADING);
                        holder.downloadBtn.setMaxProgress(100);
                        holder.downloadBtn.setProgress((int) (percent * 100));
                        holder.downloadBtn.setCurrentText(mContext.getString(R.string.pause));
                        Log.d(TAG, String.format("progress sofar: %d total: %d", soFarBytes, totalBytes));
                       // ((ViewHolder) task.getTag()).updateProgress(soFarBytes, totalBytes,
                               // task.getSpeed());
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        Log.d(TAG, String.format("error %s", e.toString()));
                        //((ViewHolder) task.getTag()).updateError(e, task.getSpeed());
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        Log.d(TAG, String.format("connected ..."));
                       // ((ViewHolder) task.getTag()).updateConnected(etag, task.getFilename());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                        Log.d(TAG, "paused !!!");
                        final float percent = soFarBytes / (float) totalBytes;
                        holder.downloadBtn.setState(DownloadProgressButton.STATE_PAUSE);
                        holder.downloadBtn.setMaxProgress(100);
                        holder.downloadBtn.setProgress((int) (percent * 100));
                        holder.downloadBtn.setCurrentText(mContext.getString(R.string.continued));
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        Log.d(TAG, "completed !!!");
                       holder.downloadBtn.setMaxProgress(1);
                       holder.downloadBtn.setProgress(1);
                       holder.downloadBtn.setState(DownloadProgressButton.STATE_FINISH);
                       holder.downloadBtn.setCurrentText(mContext.getString(R.string.install));
//                       String path =  task.getPath()+File.separator+task.getFilename();
//                        String outputDir =  task.getPath()+File.separator+"output";
//                      unzipFile( new File(path), new File(outputDir));
                      addInstalledAppToDB(item,downloadTask);
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                              holder.downloadBtn.setState(DownloadProgressButton.STATE_INSTALLED);
                               holder.downloadBtn.setCurrentText(mContext.getString(R.string.open));
                           }
                       },2000);
                        //AppUtil.toastSuccess(context, "下载成功...");
                        //((ViewHolder) task.getTag()).updateCompleted(task);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                       // ((ViewHolder) task.getTag()).updateWarn();
                    }
                });
    }

    private void handleState(ViewHolder holder, Dapp dapp){
        int state=holder.downloadBtn.getState();
        switch (state){
            case DownloadProgressButton.STATE_NORMAL:
            {
                downloadTask = createDownloadTask(holder,dapp);
                downloadTask.start();
            }
                break;
            case DownloadProgressButton.STATE_DOWNLOADING:
            {
               downloadTask.pause();
            }
                break;
            case DownloadProgressButton.STATE_PAUSE:
            {
               downloadTask.start();
               downloadTask.getId();
            }
                break;
            case DownloadProgressButton.STATE_FINISH:
            {
               // AppUtil.toastSuccess(mContext, "开始安装");

            }
                break;
            case DownloadProgressButton.STATE_INSTALLED:
            {
               TaskModel model = TasksDBContraller.getImpl().getTaskByDappId(dapp.getTransactionId());
                //String path =  downloadTask.getPath()+File.separator+"output"+File.separator+"www/index.html";
                String path ="file://"+model.getPath()+File.separator+"www/index.html";
                gotoDapp(path);
                 AppUtil.toastSuccess(mContext, "打开应用...");
            }
            break;

        }
    }

    private void gotoDapp(String path){
        Intent intent=new Intent(mContext,BaseCordovaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url",path);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    protected void unzipFile( File zipFile, File destination ) {
        UnZip decomp = new UnZip( zipFile.getPath(),
                destination.getPath() + File.separator );
        decomp.unzip();
    }

    private void addInstalledAppToDB(Dapp dapp, BaseDownloadTask task){
        String path =  task.getPath()+File.separator+task.getFilename();
        String outputDir = task.getPath()+File.separator+dapp.getTransactionId();
        unzipFile( new File(path), new File(outputDir));

        TaskModel model = new TaskModel();
        model.setId(task.getId());
        model.setDappID(dapp.getTransactionId());
        model.setName(dapp.getName());
        model.setPath(outputDir);
        model.setDapp(dapp);
        model.setUrl(dapp.getLink());
        TasksDBContraller.getImpl().addTask(model, new TasksDBContraller.OnAddTaskListener() {
            @Override
            public void onAddTask(TaskModel taskModel) {
                EventBus.getDefault().post(new DAppChangeEvent());
            }
        });


    }


    private FileDownloadListener taskDownloadListener = new FileDownloadSampleListener() {

        private ViewHolder checkCurrentHolder(final BaseDownloadTask task) {
            final ViewHolder tag = (ViewHolder) task.getTag();
            if (tag.getId()!= task.getId()) {
                return null;
            }

            return tag;
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            final ViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloading(FileDownloadStatus.pending, soFarBytes
                    , totalBytes);
            //tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_pending);
        }

        @Override
        protected void started(BaseDownloadTask task) {
            super.started(task);
            final ViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

           // tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_started);
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            final ViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloading(FileDownloadStatus.connected, soFarBytes
                    , totalBytes);
            //tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_connected);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            final ViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }


            tag.updateDownloading(FileDownloadStatus.progress, soFarBytes
                    , totalBytes);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            final ViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes()
                    , task.getLargeFileTotalBytes());
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            final ViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
           // tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_paused);
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            final ViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloaded();
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }
    };



    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.description_tv)
        TextView descriptionTv;
        @BindView(R.id.download_btn)
        DownloadProgressButton downloadBtn;
        @BindView(R.id.delete_btn)
        Button deleteBtn;

        /**
         * viewHolder position
         */
        private int pos;
        /**
         * download id
         */
        private int id;


        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

            downloadBtn.setShowBorder(true);
            downloadBtn.setButtonRadius( ConvertUtils.dp2px(20));
            //downloadBtn.setCurrentText("");
        }

        public void update(final int id, final int position) {
            this.id = id;
            this.pos = position;
        }


        public void updateDownloaded() {
            downloadBtn.setMaxProgress(1);
            downloadBtn.setProgress(1);
            downloadBtn.setState(DownloadProgressButton.STATE_FINISH);
        }

        public void updateNotDownloaded(final int status, final long sofar, final long total) {
            if (sofar > 0 && total > 0) {
                final float percent = sofar
                        / (float) total;
                downloadBtn.setMaxProgress(100);
                downloadBtn.setProgress((int) (percent * 100));
            } else {
                downloadBtn.setMaxProgress(1);
                downloadBtn.setProgress(0);
            }

//            switch (status) {
//                case FileDownloadStatus.error:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_error);
//                    break;
//                case FileDownloadStatus.paused:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_paused);
//                    break;
//                default:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_not_downloaded);
//                    break;
//            }
            downloadBtn.setText(R.string.start);
        }

        public void updateDownloading(final int status, final long sofar, final long total) {
            final float percent = sofar
                    / (float) total;
            downloadBtn.setMaxProgress(100);
            downloadBtn.setProgress((int) (percent * 100));

//            switch (status) {
//                case FileDownloadStatus.pending:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_pending);
//                    break;
//                case FileDownloadStatus.started:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_started);
//                    break;
//                case FileDownloadStatus.connected:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_connected);
//                    break;
//                case FileDownloadStatus.progress:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_progress);
//                    break;
//                default:
//                    taskStatusTv.setText(DemoApplication.CONTEXT.getString(
//                            R.string.tasks_manager_demo_status_downloading, status));
//                    break;
//            }

            downloadBtn.setText(R.string.pause);
        }
    }
}
