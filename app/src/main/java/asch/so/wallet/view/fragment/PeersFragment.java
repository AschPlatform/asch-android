package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.PeesContact;
import asch.so.wallet.model.entity.PeerNode;
import asch.so.wallet.presenter.PeersPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.PeersAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by kimziv on 2017/11/24.
 */

public class PeersFragment extends BaseFragment implements PeesContact.View{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    PeersAdapter adapter;
    PeesContact.Presenter presenter;


    public static PeersFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PeersFragment fragment = new PeersFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter =new PeersAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_peers,container,false);
        ButterKnife.bind(this,rootView);
        presenter=new PeersPresenter(getContext(),this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
//                PeerNode peerNode = adapter.getItem(position);
//                String json = JSON.toJSONString(peerNode);
//                Bundle bundle=new Bundle();
//                bundle.putString("transaction",json);
//                BaseActivity.start(getActivity(), TransactionDetailActivity.class,bundle);
//            }
//        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.loadFirstPagePeers();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                presenter.loadMorePagePeers();
            }
        });
        refreshLayout.autoRefresh();
        return rootView;
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
    public void setPresenter(PeesContact.Presenter presenter) {
            this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            AppUtil.toastError(getContext(),exception==null?"网络错误":exception.getMessage());
        }
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }

    }

    @Override
    public void displayFirstPagePeers(List<PeerNode> peers) {
            if (peers.isEmpty()) {
                loadingLayout.showEmpty();
            }else {
                loadingLayout.showContent();
            }
            adapter.replaceData(peers);
            refreshLayout.finishRefresh(500);
    }

    @Override
    public void displayMorePagePeers(List<PeerNode> peers) {
            adapter.addData(peers);
            refreshLayout.finishLoadmore(500);
    }
}
