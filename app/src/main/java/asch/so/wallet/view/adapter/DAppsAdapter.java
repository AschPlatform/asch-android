package asch.so.wallet.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.HashMap;

import asch.so.wallet.R;
import asch.so.wallet.miniapp.download.TaskModel;
import asch.so.wallet.miniapp.download.TasksManager;
import asch.so.wallet.model.entity.Dapp;
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

    public DAppsAdapter(Context context) {
        super(R.layout.item_dapps);
        this.context = context;
        tasksMap=new HashMap<>();
    }

    @Override
    protected void convert(ViewHolder holder, Dapp item) {
        holder.nameTv.setText(item.getName());
        holder.descriptionTv.setText(item.getName());
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleState(holder,item);
            }
        });

        if (TasksManager.getImpl().isReady()) {
            if (tasksMap.containsKey(item.getTransactionId())){

            TaskModel model=tasksMap.get(item.getTransactionId());
            final int status = TasksManager.getImpl().getStatus(model.getId(), model.getPath());
            if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                    status == FileDownloadStatus.connected) {
                // start task, but file not created yet
                holder.updateDownloading(status, TasksManager.getImpl().getSoFar(model.getId())
                        , TasksManager.getImpl().getTotal(model.getId()));
            } else if (!new File(model.getPath()).exists() &&
                    !new File(FileDownloadUtils.getTempPath(model.getPath())).exists()) {
                // not exist file
                holder.updateNotDownloaded(status, 0, 0);
            } else if (TasksManager.getImpl().isDownloaded(status)) {
                // already downloaded and exist
                holder.updateDownloaded();
            } else if (status == FileDownloadStatus.progress) {
                // downloading
                holder.updateDownloading(status, TasksManager.getImpl().getSoFar(model.getId())
                        , TasksManager.getImpl().getTotal(model.getId()));
            } else {

                // not start
                holder.updateNotDownloaded(status, TasksManager.getImpl().getSoFar(model.getId())
                        , TasksManager.getImpl().getTotal(model.getId()));
            }

            }else{

            }
        } else {
//            holder.taskStatusTv.setText(R.string.tasks_manager_demo_status_loading);
            holder.downloadBtn.setText(R.string.tasks_manager_demo_status_loading);
            //holder.downloadBtn.setEnabled(false);
        }
    }

    private void handleState(ViewHolder holder, Dapp dapp){
        int state=holder.downloadBtn.getState();
        switch (state){
            case DownloadProgressButton.STATE_NORMAL:
            {
                final TaskModel model = TasksManager.getImpl().get(holder.getPos());
                final BaseDownloadTask task = FileDownloader.getImpl().create(model.getUrl())
                        .setPath(model.getPath())
                        .setCallbackProgressTimes(100)
                        .setListener(taskDownloadListener);

                TasksManager.getImpl()
                        .addTaskForViewHolder(task);

                TasksManager.getImpl()
                        .updateViewHolder(holder.getId(), holder);

                task.start();
            }
                break;
            case DownloadProgressButton.STATE_DOWNLOADING:
            {
                //FileDownloader.getImpl().pause()
            }
                break;
            case DownloadProgressButton.STATE_PAUSE:
            {
                TasksManager.getImpl().getTaskByDappId(dapp.getTransactionId());
            }
                break;
            case DownloadProgressButton.STATE_FINISH:
            {

            }
                break;
        }
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
            downloadBtn.setCurrentText("安装");
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
