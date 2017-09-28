package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import asch.so.wallet.activity.AccountsActivity;
import asch.so.wallet.contract.MineContract;
import asch.so.wallet.view.adapter.MineAdapter;
import asch.so.wallet.view.entity.MineItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class MineFragment extends BaseFragment implements MineContract.View{

    MineContract.Presenter presenter;
    @BindView(R.id.mine_rcv)
    RecyclerView mineRcv;


    private List<MineItem> itemList=new ArrayList<>();
    private MineAdapter adapter =new MineAdapter(itemList);

    public static MineFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container,false);
        ButterKnife.bind(this,rootView);
        mineRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        mineRcv.setItemAnimator(new DefaultItemAnimator());
//        itemList=new ArrayList<>();
//        adapter =new MineAdapter(itemList);
        mineRcv.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
                switch (position){
                    case 0:
                    {
                        Intent intent = new Intent(getActivity(), AccountsActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
            }
        });
        return rootView;
    }


    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayItems(List<MineItem> items) {
        this.itemList.clear();
        this.itemList.addAll(items);
        this.adapter.notifyDataSetChanged();
    }
}
