package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.FileUtils;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.activity.BaseCordovaActivity;
import asch.io.wallet.activity.DAppBalanceActivity;
import asch.io.wallet.activity.DAppDepositActivity;
import asch.io.wallet.contract.DAppDetailContract;
import asch.io.wallet.event.DAppDownloadEvent;
import asch.io.wallet.miniapp.download.DownloadExtraStatus;
import asch.io.wallet.miniapp.download.Downloader;
import asch.io.wallet.miniapp.download.DownloadsManager;
import asch.io.wallet.model.entity.DApp;
import asch.io.wallet.presenter.DAppDetailPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.widget.downloadbutton.DownloadProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2018/1/31.
 */

public class DAppDetailFragment extends BaseFragment implements DAppDetailContract.View{

    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.description_tv)
    TextView descriptionTv;
    @BindView(R.id.deposit_btn)
    Button depositBtn;
    @BindView(R.id.balance_btn)
    Button balanceBtn;
    @BindView(R.id.download_btn)
    DownloadProgressButton downloadBtn;
    Unbinder unbinder;

    private DAppDetailContract.Presenter presenter;
    private String dappId;
    private DApp dapp;
    private Downloader downloader;
    public static final int UNINSTALLED=0;
    public static final int INSATLLED=1;
    private int type;//uninstalled:0, intalled:1

    public static DAppDetailFragment newInstance(Bundle args) {
        DAppDetailFragment fragment = new DAppDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static DAppDetailFragment newInstance(String dappId) {
        
        Bundle args = new Bundle();

        args.putString("dapp_id",dappId);
        DAppDetailFragment fragment = new DAppDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DAppDetailFragment newInstance(DApp dapp) {

        Bundle args = new Bundle();
        String json = JSON.toJSONString(dapp);
        args.putString("dapp",json);

        DAppDetailFragment fragment = new DAppDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey("dapp")){
            String dappJson=getArguments().getString("dapp");
            dapp= JSON.parseObject(dappJson,DApp.class);
            type=UNINSTALLED;
        }else {
            dappId=getArguments().getString("dapp_id");
            type=INSATLLED;
            presenter=new DAppDetailPresenter(getContext(),this);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_dapp_detail,container,false);
        unbinder = ButterKnife.bind(this, rootView);

        depositBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), DAppDepositActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("dapp_id",dappId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        balanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), DAppBalanceActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("dapp_id",dappId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleState(dapp,downloader);
            }
        });

        if (type==UNINSTALLED){
           Downloader downloader = DownloadsManager.getImpl().getDownloader(dapp);
            nameTv.setText(dapp.getName());
            descriptionTv.setText(dapp.getDescription());
            updateView(dapp.getStatus(),downloader.getSoFar(),downloader.getTotal());

        }else {
            presenter.loadDApp(dappId);
        }


        return rootView;
    }


    private void gotoDapp(String path){
        Intent intent=new Intent(getContext(),BaseCordovaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url",path);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DAppDownloadEvent event){
        if (downloader!=null && downloader.getDownloadId() == event.getDownloadId())
        {
            updateView(event.getStatus(),event.getSoFarBytes(),event.getTotalBytes());
        }
    }

    private void handleState(DApp dapp, Downloader downloader){
        int state=downloadBtn.getState();
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
                String path= dapp.getInstalledPath();
                if (FileUtils.isFileExists(path) && FileUtils.isDir(path)) {
                    String wwwPath = "file://" + path + File.separator + "www/index.html";
                    gotoDapp(wwwPath);
                    AppUtil.toastSuccess(getContext(), "打开应用...");
                }
            }
            break;

        }
    }

    public void updateView(int status, long sofarBytes, long totalBytes){
        switch (status){
            case FileDownloadStatus.INVALID_STATUS:{
                downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
                downloadBtn.setCurrentText(getActivity().getString(R.string.download));
            }
            case  FileDownloadStatus.connected:
            {

            }
            break;
            case FileDownloadStatus.started:
            {
                downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
                updateNotDownloaded(FileDownloadStatus.started,  sofarBytes, totalBytes);
            }
            break;
            case FileDownloadStatus.error:
            {
                //Log.d(TAG, String.format("error %s", e.toString()));
            }
            break;
            case FileDownloadStatus.progress:
            {
                updateDownloading(FileDownloadStatus.progress, sofarBytes, totalBytes);
                final float percent = sofarBytes / (float) totalBytes;
                downloadBtn.setState(DownloadProgressButton.STATE_DOWNLOADING);
                downloadBtn.setMaxProgress(100);
                downloadBtn.setProgress((int) (percent * 100));
                downloadBtn.setCurrentText(getActivity().getString(R.string.pause));
            }
            break;
            case FileDownloadStatus.paused:
            {
                updateNotDownloaded(FileDownloadStatus.paused,  sofarBytes, totalBytes);
                //Log.d(TAG, "paused !!!");
                final float percent = sofarBytes / (float) totalBytes;
                downloadBtn.setState(DownloadProgressButton.STATE_PAUSE);
                downloadBtn.setMaxProgress(100);
                downloadBtn.setProgress((int) (percent * 100));
                downloadBtn.setCurrentText(getActivity().getString(R.string.continued));

            }
            break;
            case FileDownloadStatus.completed:
            {
                downloadBtn.setMaxProgress(1);
                downloadBtn.setProgress(1);
                downloadBtn.setState(DownloadProgressButton.STATE_FINISH);
                downloadBtn.setCurrentText(getActivity().getString(R.string.install));
            }
            break;
            case DownloadExtraStatus.INSTALLED:
            {
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                        downloadBtn.setState(DownloadProgressButton.STATE_INSTALLED);
                        downloadBtn.setCurrentText(getActivity().getString(R.string.open));
                   // }
               // },1600);

            }
            break;
            case DownloadExtraStatus.UNINSTALLED:
            {
                downloadBtn.setState(DownloadProgressButton.STATE_NORMAL);
                downloadBtn.setCurrentText(getActivity().getString(R.string.download));
            }
            break;
        }
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

        downloadBtn.setCurrentText(getString(R.string.start_download));
    }

    public void updateDownloading(final int status, final long sofar, final long total) {
        final float percent = sofar
                / (float) total;
        downloadBtn.setMaxProgress(100);
        downloadBtn.setProgress((int) (percent * 100));
        downloadBtn.setText(R.string.pause);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (type==INSATLLED){
            presenter.unSubscribe();
        }
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();
        registerSubcriber();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterSubcriber();
    }

    private void registerSubcriber(){
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void unregisterSubcriber(){
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }



    @Override
    public void setPresenter(DAppDetailContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(Throwable exception) {

    }

    @Override
    public void displayDApp(DApp dapp) {
        this.dapp=dapp;
        this.downloader = DownloadsManager.getImpl().getDownloader(dapp);
        nameTv.setText(dapp.getName());
        descriptionTv.setText(dapp.getDescription());
        //downloadBtn.setState(DownloadProgressButton.STATE_INSTALLED);
        //downloadBtn.setCurrentText("打开应用");
        updateView(dapp.getStatus(),  downloader.getSoFar(), downloader.getTotal());
    }
}
