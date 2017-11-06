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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.UIException;
import asch.so.wallet.R;
import asch.so.wallet.activity.AssetReceiveActivity;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.activity.WebActivity;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.view.adapter.AssetTransactionsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.impl.AschConst;

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

    private Balance balance;

    private AssetTransactionsContract.Presenter presenter;
    private List<Transaction> list=new ArrayList<>();
    private AssetTransactionsAdapter adapter=new AssetTransactionsAdapter();


    public static AssetTransactionsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetTransactionsFragment fragment = new AssetTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        balance= (Balance) JSON.parseObject(getArguments().getString("balance"),Balance.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transactions,container,false);
        ButterKnife.bind(this,rootView);

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
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent =new Intent(getContext(),WebActivity.class);
                startActivity(intent);
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
                presenter.loadTransactions(balance.getCurrency(), !AschConst.CORE_COIN_NAME.equals(balance.getCurrency()));
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
        refreshLayout.autoRefresh();

        this.displayBalance(balance);

        return rootView;
    }

    @Override
    public void setPresenter(AssetTransactionsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(UIException exception) {
        Toast.makeText(getContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
        refreshLayout.finishRefresh(1000);
    }

    @Override
    public void displayBalance(Balance balance) {
        amountTv.setText(String.valueOf(balance.getRealBalance()));
        assetTv.setText(balance.getCurrency()+" 余额");
    }

    @Override
    public void displayTransactions(List<Transaction> transactions) {
        adapter.replaceData(transactions);
        refreshLayout.finishRefresh(1000);
    }

    @Override
    public void displayMoreTransactions(List<Transaction> transactions) {

    }
}
