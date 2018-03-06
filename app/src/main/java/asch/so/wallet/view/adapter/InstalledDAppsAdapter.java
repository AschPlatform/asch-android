package asch.so.wallet.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import asch.so.wallet.R;
import asch.so.wallet.activity.BaseCordovaActivity;
import asch.so.wallet.event.DAppChangeEvent;
import asch.so.wallet.miniapp.download.DownloadExtraStatus;
import asch.so.wallet.miniapp.download.Downloader;
import asch.so.wallet.miniapp.download.DownloadsDB;
import asch.so.wallet.miniapp.download.DownloadsManager;
import asch.so.wallet.model.entity.DApp;
import asch.so.wallet.util.AppUtil;
import asch.so.widget.downloadbutton.DownloadProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2018/1/19.
 */

public class InstalledDAppsAdapter extends BaseQuickAdapter<DApp, InstalledDAppsAdapter.ViewHolder> {

    private Context context;

    public InstalledDAppsAdapter(Context context) {
        super(R.layout.item_installed_dapps);
        this.context = context;
    }

    private void gotoDapp(String path){
        Intent intent=new Intent(mContext,BaseCordovaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url",path);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    protected void convert(ViewHolder holder, DApp dapp) {
        holder.nameTv.setText(dapp.getName());
        holder.descriptionTv.setText(dapp.getName());
        holder.downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
        holder.downloadBtn.setCurrentText(mContext.getString(R.string.open));
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dapp.getStatus()== DownloadExtraStatus.INSTALLED){
                    String path= dapp.getInstalledPath();
                    if (FileUtils.isFileExists(path) && FileUtils.isDir(path)){
                        String wwwPath ="file://"+dapp.getInstalledPath()+File.separator+"www/index.html";
                        gotoDapp(wwwPath);
                        return;
                    }
                }
                AppUtil.toastInfo(mContext,"请先下载安装, 再使用");
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Downloader downloader= DownloadsManager.getImpl().getDownloader(dapp);
                downloader.uninstall();
                AppUtil.toastInfo(context,context.getString(R.string.uninstall_success));
            }
        });
    }

    public static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.description_tv)
        TextView descriptionTv;
        @BindView(R.id.download_btn)
        DownloadProgressButton downloadBtn;
        @BindView(R.id.delete_btn)
        Button deleteBtn;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            downloadBtn.setShowBorder(true);
            downloadBtn.setButtonRadius( ConvertUtils.dp2px(20));
        }
    }
}
