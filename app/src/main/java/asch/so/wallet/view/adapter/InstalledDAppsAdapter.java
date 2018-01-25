package asch.so.wallet.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liulishuo.filedownloader.DownloadTask;

import java.io.File;
import java.util.List;

import asch.so.wallet.R;
import asch.so.wallet.activity.BaseCordovaActivity;
import asch.so.wallet.miniapp.download.TaskModel;
import asch.so.wallet.miniapp.download.TasksDBContraller;
import asch.so.wallet.model.entity.Dapp;
import asch.so.widget.downloadbutton.DownloadProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2018/1/19.
 */

public class InstalledDAppsAdapter extends BaseQuickAdapter<TaskModel, InstalledDAppsAdapter.ViewHolder> {

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
    protected void convert(ViewHolder holder, TaskModel item) {
        Dapp dapp=item.getDapp();

        holder.nameTv.setText(dapp.getName());
        holder.descriptionTv.setText(dapp.getName());
        holder.downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
        holder.downloadBtn.setCurrentText(mContext.getString(R.string.open));
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path ="file://"+item.getPath()+File.separator+"www/index.html";
                gotoDapp(path);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new File(DOWNLOAD_PATH).delete();
//                holder.downloadBtn.setMaxProgress(1);
//                holder.downloadBtn.setProgress(0);
//                holder.downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
//                holder.downloadBtn.setCurrentText(mContext.getString(R.string.download));
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
