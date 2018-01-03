package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.activity.TransactionDetailActivity;
import asch.so.wallet.contract.TransactionsContract;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.TransactionsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by kimziv on 2017/10/18.
 */

public class TransactionsFragment extends BaseFragment implements TransactionsContract.View{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    TransactionsAdapter adapter;
    TransactionsContract.Presenter presenter;



    public static TransactionsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TransactionsFragment fragment = new TransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter =new TransactionsAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transactions,container,false);
        ButterKnife.bind(this,rootView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                Transaction transaction = adapter.getItem(position);
                String json = JSON.toJSONString(transaction);
                Bundle bundle=new Bundle();
                bundle.putString("transaction",json);
                BaseActivity.start(getActivity(), TransactionDetailActivity.class,bundle);
            }
        });

       // materialHeader = (MaterialHeader)refreshLayout.getRefreshHeader();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.loadFirstPageTransactions();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                presenter.loadMorePageTransactions();
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
    public void setPresenter(TransactionsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            AppUtil.toastError(getContext(),exception==null?getString(R.string.net_error):exception.getMessage());
        }
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }

    }

    @Override
    public void displayFirstPageTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            loadingLayout.showEmpty();
        }else {
            loadingLayout.showContent();
        }
        adapter.replaceData(transactions);
        refreshLayout.finishRefresh(500);



    }

    @Override
    public void displayMorePageTransactions(List<Transaction> transactions) {
        adapter.addData(transactions);
        refreshLayout.finishLoadmore(500);
    }
}
