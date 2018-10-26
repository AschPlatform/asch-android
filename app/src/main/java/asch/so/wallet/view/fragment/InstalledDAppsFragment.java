package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.DAppDetailActivity;
import asch.so.wallet.contract.InstalledDappsContract;
import asch.so.wallet.event.DAppChangeEvent;
import asch.so.wallet.event.DAppDownloadEvent;
import asch.so.wallet.miniapp.download.DownloadExtraStatus;
import asch.so.wallet.model.entity.DApp;
import asch.so.wallet.presenter.InstalledDappsPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.InstalledDAppsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by kimziv on 2018/1/19.
 */

public class InstalledDAppsFragment extends BaseFragment implements InstalledDappsContract.View{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    Unbinder unbinder;
    private InstalledDappsContract.Presenter presenter;
    private InstalledDAppsAdapter adapter;

    public static InstalledDAppsFragment newInstance() {

        Bundle args = new Bundle();

        InstalledDAppsFragment fragment = new InstalledDAppsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter=new InstalledDappsPresenter(getContext(),this);
        adapter=new InstalledDAppsAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_installed_dapps, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.loadInstalledDapps();
            }
        });
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.autoRefresh();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position==0){

                    //BaseActivity.start(getActivity(),DAppDetailActivity.class,);
                    DApp dapp=(DApp) adapter.getItem(position);
                    Intent intent=new Intent(getContext(),DAppDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("dapp_id",dapp.getTransactionId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DAppDownloadEvent event) {
        if (event.getStatus()== DownloadExtraStatus.INSTALLED||event.getStatus()==DownloadExtraStatus.UNINSTALLED){
            presenter.loadInstalledDapps();
        }
    }

    public void lodaDApps(){
        presenter.loadInstalledDapps();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void setPresenter(InstalledDappsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            AppUtil.toastError(getContext(),AppUtil.extractInfoFromError(getContext(),exception));
        }
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }

    }

    @Override
    public void displayInstalledDapps(List<DApp> dapps) {
        if (dapps.isEmpty()) {
            loadingLayout.showEmpty();
        }else {
            loadingLayout.showContent();
        }
        adapter.replaceData(dapps);
        refreshLayout.finishRefresh(500);
    }
}
