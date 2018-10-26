package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.contract.DAppBalanceContract;
import asch.so.wallet.model.entity.DAppBalance;
import asch.so.wallet.presenter.DAppBalancePresenter;
import asch.so.wallet.presenter.PeersPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.DAppBalanceAdapter;
import asch.so.wallet.view.adapter.PeersAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by kimziv on 2018/2/9.
 */

public class DAppBalanceFragment extends BaseFragment implements DAppBalanceContract.View{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    Unbinder unbinder;

    DAppBalanceAdapter adapter;
    DAppBalanceContract.Presenter presenter;
    String dappId;

    public static DAppBalanceFragment newInstance(String dappId) {
        
        Bundle args = new Bundle();
        args.putString("dapp_id",dappId);
        DAppBalanceFragment fragment = new DAppBalanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dappId=getArguments().getString("dapp_id");
        adapter =new DAppBalanceAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_dapp_balance,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        presenter=new DAppBalancePresenter(getContext(),this,dappId);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.loadFirstPageBalances();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                presenter.loadMorePageBalances();
            }
        });
        refreshLayout.autoRefresh();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(DAppBalanceContract.Presenter presenter) {
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
    public void displayFirstPageBalances(List<DAppBalance> peers) {
        if (peers.isEmpty()) {
            loadingLayout.showEmpty();
        }else {
            loadingLayout.showContent();
        }
        adapter.replaceData(peers);
        refreshLayout.finishRefresh(500);
    }

    @Override
    public void displayMorePageBalances(List<DAppBalance> peers) {
        adapter.addData(peers);
        refreshLayout.finishLoadmore(500);
    }
}
