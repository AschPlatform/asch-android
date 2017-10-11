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

import java.util.ArrayList;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.DappActivity;
import asch.so.wallet.contract.DappCenterContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.view.adapter.AssetsAdapter;
import asch.so.wallet.view.adapter.DappCenterAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/11.
 */

public class DappCenterFragment extends BaseFragment implements DappCenterContract.View{

    @BindView(R.id.dapp_list_rv)
    RecyclerView dappListRv;

    DappCenterContract.Presenter presenter;

    private List<Dapp> dappList=new ArrayList<>();
    private DappCenterAdapter adapter=new DappCenterAdapter(dappList);


    public static DappCenterFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DappCenterFragment fragment = new DappCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_dapp_center,container,false);
        ButterKnife.bind(this,rootView);
        dappListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        dappListRv.setItemAnimator(new DefaultItemAnimator());
        dappListRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
                Intent intent=new Intent(getActivity(), DappActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setPresenter(DappCenterContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayDappList(List<Dapp> dapps) {
        this.dappList.clear();
        this.dappList.addAll(dapps);
        adapter.notifyDataSetChanged();
    }
}
