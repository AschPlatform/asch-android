package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.BaseCordovaActivity;
import asch.so.wallet.activity.DAppBalanceActivity;
import asch.so.wallet.activity.DAppDepositActivity;
import asch.so.wallet.contract.DAppDepositContract;
import asch.so.wallet.contract.DAppDetailContract;
import asch.so.wallet.miniapp.download.TaskModel;
import asch.so.wallet.miniapp.download.TasksDBContraller;
import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.presenter.DAppDetailPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.widget.downloadbutton.DownloadProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.open_dapp_btn)
    Button openDappBtn;
    private DAppDetailContract.Presenter presenter;
    private String dappId;



    public static DAppDetailFragment newInstance(String dappId) {
        
        Bundle args = new Bundle();
        args.putString("dapp_id",dappId);
        DAppDetailFragment fragment = new DAppDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dappId=getArguments().getString("dapp_id");
        presenter=new DAppDetailPresenter(getContext(),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_dapp_detail,container,false);
        ButterKnife.bind(this,rootView);

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

        openDappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskModel model = TasksDBContraller.getImpl().getTaskByDappId(dappId);
                //String path =  downloadTask.getPath()+File.separator+"output"+File.separator+"www/index.html";
                String path ="file://"+model.getPath()+ File.separator+"www/index.html";
                gotoDapp(path);
                AppUtil.toastSuccess(getContext(), "打开应用...");
            }
        });

        presenter.loadDApp(dappId);

        return rootView;
    }


    private void gotoDapp(String path){
        Intent intent=new Intent(getContext(),BaseCordovaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url",path);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void setPresenter(DAppDetailContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(Throwable exception) {

    }

    @Override
    public void displayDApp(TaskModel taskModel) {
        Dapp dapp=taskModel.getDapp();
        nameTv.setText(dapp.getName());
        descriptionTv.setText(dapp.getDescription());

    }
}
