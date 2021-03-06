package asch.io.wallet.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import asch.io.wallet.R;
import asch.io.wallet.activity.BaseCordovaActivity;
import asch.io.wallet.event.DAppDownloadEvent;
import asch.io.wallet.miniapp.download.DownloadExtraStatus;
import asch.io.wallet.miniapp.download.Downloader;
import asch.io.wallet.miniapp.download.DownloadsManager;
import asch.io.wallet.model.entity.DApp;
import asch.io.wallet.util.AppUtil;
import asch.io.widget.downloadbutton.DownloadProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2018/1/19.
 */

public class DAppsAdapter extends BaseQuickAdapter<DApp, DAppsAdapter.ViewHolder> {
    private static final String TAG=DAppsAdapter.class.getSimpleName();
    private Context context;
    private static final String DOWNLOAD_URL="http://asch-public.oss-cn-beijing.aliyuncs.com/appupdate/test/www2.zip";

    public DAppsAdapter(Context context) {
        super(R.layout.item_dapps);
        this.context = context;
    }

    @Override
    protected void convert(DAppsAdapter.ViewHolder holder, DApp item) {
        item.setLink(DOWNLOAD_URL);
        registerSubcriber(holder);
        holder.setContext(this.context);

        Downloader downloader= DownloadsManager.getImpl().getDownloader(item);
        DApp dapp=downloader.getDapp();
        holder.setDapp(dapp);
        holder.setDownloader(downloader);
        holder.nameTv.setText(dapp.getName());
        holder.descriptionTv.setText(dapp.getName());

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleState(holder,dapp, downloader);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 downloader.uninstall();
                AppUtil.toastInfo(context,context.getString(R.string.uninstall_success));
            }
        });

        int status = dapp.getStatus();
        long sofarBytes=downloader.getSoFar();
        long totalBytes=downloader.getTotal();
         holder.updateView(status, sofarBytes, totalBytes);

    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
       registerSubcriber(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        unregisterSubcriber(holder);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    private void registerSubcriber(ViewHolder holder){
        if (!EventBus.getDefault().isRegistered(holder)){
            EventBus.getDefault().register(holder);
        }
    }

    private void unregisterSubcriber(ViewHolder holder){
        if (EventBus.getDefault().isRegistered(holder)){
            EventBus.getDefault().unregister(holder);
        }
    }


    private void handleState(ViewHolder holder, DApp dapp, Downloader downloader){
        int state=holder.downloadBtn.getState();
        switch (state){
            case DownloadProgressButton.STATE_NORMAL:
            {
                downloader.start();
            }
                break;
            case DownloadProgressButton.STATE_DOWNLOADING:
            {
                downloader.pause();
            }
                break;
            case DownloadProgressButton.STATE_PAUSE:
            {
                downloader.resume();
            }
                break;
            case DownloadProgressButton.STATE_FINISH:
            {
               // AppUtil.toastSuccess(mContext, "开始安装");

            }
                break;
            case DownloadProgressButton.STATE_INSTALLED:
            {
               //TaskModel model = TasksDBContraller.getImpl().getTaskByDappId(dapp.getTransactionId());
                //String path =  downloadTask.getPath()+File.separator+"output"+File.separator+"www/index.html";
                String path ="file://"+downloader.getInstalledPath()+File.separator+"www/index.html";
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




    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.description_tv)
        TextView descriptionTv;
        @BindView(R.id.download_btn)
        DownloadProgressButton downloadBtn;
        @BindView(R.id.delete_btn)
        Button deleteBtn;
        private Context context;
        private DApp dapp;
        private Downloader downloader;
        private OnDownloadCompletedListener downloadCompletedListener;

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public Downloader getDownloader() {
            return downloader;
        }

        public void setDownloader(Downloader downloader) {
            this.downloader = downloader;
        }

        public OnDownloadCompletedListener getDownloadCompletedListener() {
            return downloadCompletedListener;
        }

        public void setDownloadCompletedListener(OnDownloadCompletedListener downloadCompletedListener) {
            this.downloadCompletedListener = downloadCompletedListener;
        }

        public DApp getDapp() {
            return dapp;
        }

        public void setDapp(DApp dapp) {
            this.dapp = dapp;
        }

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this,view);

            downloadBtn.setShowBorder(true);
            downloadBtn.setButtonRadius( ConvertUtils.dp2px(20));
            //downloadBtn.setCurrentText("");
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(DAppDownloadEvent event){
            if (downloader!=null && downloader.getDownloadId() == event.getDownloadId()) {
                updateView(event.getStatus(), event.getSoFarBytes(), event.getTotalBytes());
            }
        }

        public void updateView(int status, long sofarBytes, long totalBytes){
            switch (status){
                case FileDownloadStatus.INVALID_STATUS:
                {
                    downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
                    downloadBtn.setCurrentText(context.getString(R.string.download));
                }
                break;
                case  FileDownloadStatus.connected:
                {

                }
                break;
                case FileDownloadStatus.started:
                {
                    downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
                    downloadBtn.setCurrentText(context.getString(R.string.download));
                    updateNotDownloaded(FileDownloadStatus.started,  sofarBytes, totalBytes);
                }
                break;
                case FileDownloadStatus.error:
                {
                    downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
                    downloadBtn.setCurrentText(context.getString(R.string.download));
                    updateNotDownloaded(FileDownloadStatus.started,  sofarBytes, totalBytes);
                }
                break;
                case FileDownloadStatus.progress:
                {
                    final float percent = sofarBytes / (float) totalBytes;
                    downloadBtn.setState(DownloadProgressButton.STATE_DOWNLOADING);
                    downloadBtn.setMaxProgress(100);
                    downloadBtn.setProgress((int) (percent * 100));
                    downloadBtn.setCurrentText(context.getString(R.string.pause));
                }
                break;
                case FileDownloadStatus.paused:
                {

                    Log.d(TAG, "paused !!!");
                    downloadBtn.setState(DownloadProgressButton.STATE_PAUSE);
                    downloadBtn.setCurrentText(context.getString(R.string.continued));
                    updateNotDownloaded(FileDownloadStatus.paused,  sofarBytes, totalBytes);

                }
                break;
                case FileDownloadStatus.completed:
                {
                    updateDownloaded();
                    Log.d(TAG, "completed !!!");
                    downloadBtn.setMaxProgress(1);
                    downloadBtn.setProgress(1);
                    downloadBtn.setState(DownloadProgressButton.STATE_FINISH);
                    downloadBtn.setCurrentText(context.getString(R.string.install));
                }
                break;
                case DownloadExtraStatus.INSTALLED:
                {
                    //new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                       // @Override
                       // public void run() {
                            downloadBtn.setState(DownloadProgressButton.STATE_INSTALLED);
                            downloadBtn.setCurrentText(context.getString(R.string.open));
                       // }
                    //},1600);
                }
                break;
                case DownloadExtraStatus.UNINSTALLED:
                {
                    downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
                    downloadBtn.setCurrentText(context.getString(R.string.download));
                }
                break;
            }
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
        }

        public interface OnDownloadCompletedListener{
            void onDAppDownloadCompleted(ViewHolder holder, DApp dApp, Downloader downloader);
        }


    }
}
