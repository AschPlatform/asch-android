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
import android.widget.TextView;
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
import asch.so.wallet.activity.AssetReceiveActivity;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.activity.TransactionDetailActivity;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.presenter.AssetTransactionsPresenter;
import asch.so.wallet.view.adapter.AssetTransactionsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransactionsFragment extends BaseFragment implements AssetTransactionsContract.View{

    @BindView(R.id.goto_transfer_btn)
    TextView transferBtn;
    @BindView(R.id.goto_receive_btn)
    TextView receiveBtn;
    @BindView(R.id.asset_transactions_rcv)
    RecyclerView txRcv;
    TextView amountTv;
    TextView assetTv;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    private Balance balance;

    private AssetTransactionsContract.Presenter presenter;
    private AssetTransactionsAdapter adapter=null;


    public static AssetTransactionsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetTransactionsFragment fragment = new AssetTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new AssetTransactionsAdapter(getContext());
        balance= (Balance) JSON.parseObject(getArguments().getString("balance"),Balance.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transactions,container,false);
        ButterKnife.bind(this,rootView);

        presenter=new AssetTransactionsPresenter(getActivity(),this, balance.getCurrency());

        txRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        txRcv.setItemAnimator(new DefaultItemAnimator());
        txRcv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        txRcv.setAdapter(adapter);
        //添加Header
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_asset_transactions, txRcv, false);
        adapter.addHeaderView(header);
        amountTv=ButterKnife.findById(header,R.id.ammount_tv);
        assetTv=ButterKnife.findById(header,R.id.asset_tv);

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

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();
                Intent intent =new Intent(getActivity(), AssetTransferActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AssetReceiveActivity.class);
                startActivity(intent);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (presenter!=null) {
                    presenter.loadFirstPageTransactions();
                }
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (presenter!=null) {
                    presenter.loadMorePageTransactions();
                }
            }
        });
        refreshLayout.autoRefresh();

        this.displayBalance(balance);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        txRcv.setAdapter(null);

    }

    @Override
    public void setPresenter(AssetTransactionsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            if (getContext()!=null) {
                Toast.makeText(getContext(), exception == null ? "网络错误" : exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }
    }

    @Override
    public void displayBalance(Balance balance) {
        amountTv.setText(String.valueOf(balance.getRealBalance()));
        assetTv.setText(balance.getCurrency()+" 余额");
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
