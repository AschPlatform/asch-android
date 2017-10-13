package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.AssetReceiveActivity;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.view.adapter.AssetTransactionsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransactionsFragment extends BaseFragment implements AssetTransactionsContract.View{

    @BindView(R.id.goto_transfer_btn)
    Button transferBtn;
    @BindView(R.id.goto_receive_btn)
    Button receiveBtn;
    @BindView(R.id.asset_transactions_rcv)
    RecyclerView txRcv;

    private AssetTransactionsContract.Presenter presenter;
    private List<Transaction> list=new ArrayList<>();
    private AssetTransactionsAdapter adapter=new AssetTransactionsAdapter(list);


    public static AssetTransactionsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetTransactionsFragment fragment = new AssetTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transactions,container,false);
        ButterKnife.bind(this,rootView);

        txRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        txRcv.setItemAnimator(new DefaultItemAnimator());
        txRcv.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // TODO: 2017/10/13
            }
        });
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();
                Intent intent =new Intent(getActivity(), AssetTransferActivity.class);
                intent.putExtra("curreny",bundle.getString("curreny"));
                intent.putExtra("precision",bundle.getInt("precision"));
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
        return rootView;
    }

    @Override
    public void setPresenter(AssetTransactionsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayTransactions(List<Transaction> transactions) {
        list.clear();
        list.addAll(transactions);
        adapter.notifyDataSetChanged();
    }
}
