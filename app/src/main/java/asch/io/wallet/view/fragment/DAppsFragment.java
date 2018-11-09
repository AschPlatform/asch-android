package asch.io.wallet.view.fragment;

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
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.activity.DAppDetailActivity;
import asch.io.wallet.contract.DappsContract;
import asch.io.wallet.model.entity.DApp;
import asch.io.wallet.presenter.DappsPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.adapter.DAppsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by kimziv on 2018/1/19.
 */

public class DAppsFragment extends BaseFragment implements DappsContract.View{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    Unbinder unbinder;

    DappsContract.Presenter presenter;
    DAppsAdapter adapter;

    public static DAppsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DAppsFragment fragment = new DAppsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter =new DAppsAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dapps, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter=new DappsPresenter(getContext(),this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.loadFirstPageDapps();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                presenter.loadMorePageDapps();
            }
        });
        refreshLayout.autoRefresh();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position==0){

                    DApp dapp =(DApp) adapter.getItem(position);
                    Bundle bundle=new Bundle();
                    bundle.putString("dapp_id", dapp.getTransactionId());
                    Intent intent=new Intent(getContext(),DAppDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    @Override
    public void setPresenter(DappsContract.Presenter presenter) {
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
    public void displayFirstPageDapps(List<DApp> DApps) {
        if (DApps.isEmpty()) {
            loadingLayout.showEmpty();
        }else {
            loadingLayout.showContent();
        }
        adapter.replaceData(DApps);
        refreshLayout.finishRefresh(500);
    }

    @Override
    public void displayMorePageDapps(List<DApp> DApps) {
        adapter.addData(DApps);
        refreshLayout.finishLoadmore(500);
    }


}
