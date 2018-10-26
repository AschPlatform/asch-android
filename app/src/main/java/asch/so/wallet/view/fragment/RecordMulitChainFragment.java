package asch.so.wallet.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.TransactionDetailActivity;
import asch.so.wallet.contract.RecordMulitChainContract;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.Deposit;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.Withdraw;
import asch.so.wallet.presenter.RecordMulitChainPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.AssetTransactionsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ezy.ui.layout.LoadingLayout;

@SuppressLint("ValidFragment")
public class RecordMulitChainFragment extends BaseFragment implements RecordMulitChainContract.View {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    @BindView(R.id.asset_transactions_rcv)
    RecyclerView txRcv;
    Unbinder unbinder;
    //记录类型
    RecordMulitChainPresenter.RecordType recordType;
    private AssetTransactionsAdapter adapter;
    private RecordMulitChainContract.Presenter presenter;
    //币种的类别
    @BaseAsset.Type int assetType;
    String currency;

    public static RecordMulitChainFragment getInstance(String title,int assetType,String currency,RecordMulitChainPresenter.RecordType recordType) {
        RecordMulitChainFragment fragment = new RecordMulitChainFragment();
        fragment.assetType = assetType;
        fragment.currency = currency;
        fragment.recordType = recordType;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new AssetTransactionsAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record_mulit_chain, null);
        unbinder = ButterKnife.bind(this, v);
        txRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        txRcv.setItemAnimator(new DefaultItemAnimator());
        txRcv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        txRcv.setAdapter(adapter);
        presenter = new RecordMulitChainPresenter(getActivity(),this,assetType,currency, recordType);

        if (recordType== RecordMulitChainPresenter.RecordType.transfer)
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    Transaction transaction = (Transaction) adapter.getItem(position);
                    String json = JSON.toJSONString(transaction);
                    Bundle bundle=new Bundle();
                    bundle.putString("transaction",json);
                    BaseActivity.start(getActivity(), TransactionDetailActivity.class,bundle);
                }
            });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (presenter!=null) {
                    presenter.loadFirstPageRecords();
                }
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (presenter!=null) {
                    presenter.loadMorePageRecords();
                }
            }
        });
        refreshLayout.autoRefresh();

        presenter.loadFirstPageRecords();
        return v;
    }


    @Override
    public void displayFirstPageRecords(List<?> records) {
        if (records.isEmpty()) {
            loadingLayout.showEmpty();
        }else {
            loadingLayout.showContent();
        }
        adapter.replaceData(records);
        refreshLayout.finishRefresh(500);
    }

    @Override
    public void displayMorePageRecords(List<?> records) {
        adapter.addData(records);
        refreshLayout.finishLoadmore(500);
    }




    @Override
    public void setPresenter(RecordMulitChainContract.Presenter presenter) {
         this.presenter = presenter;
    }

    @Override
    public void displayError(Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            if (getContext()!=null) {
                AppUtil.toastError(getContext(), AppUtil.extractInfoFromError(getContext(),exception));
            }
        }
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }
}