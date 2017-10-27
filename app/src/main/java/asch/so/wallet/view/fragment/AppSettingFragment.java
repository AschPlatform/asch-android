package asch.so.wallet.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Arrays;
import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.LanguagesActivity;
import asch.so.wallet.activity.NodeURLSettingActivity;
import asch.so.wallet.activity.PincodeSettingActivity;
import asch.so.wallet.contract.AppSettingContract;
import asch.so.wallet.view.adapter.BaseRecyclerAdapter;
import asch.so.wallet.view.adapter.SmartViewHolder;
import asch.so.wallet.view.entity.SettingItem;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.layout.simple_list_item_2;

/**
 * Created by kimziv on 2017/10/16.
 */

public class AppSettingFragment extends BaseFragment implements AppSettingContract.View, AdapterView.OnItemClickListener{

    private AppSettingContract.Presenter presenter;
    private BaseRecyclerAdapter<Item> adapter;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static AppSettingFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AppSettingFragment fragment = new AppSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Item item = Item.values()[position];
        switch (item){
            case  Language:
            {
                BaseActivity.start(getActivity(), LanguagesActivity.class,null);
            }
                break;
            case NodeURL:
            {
                BaseActivity.start(getActivity(), NodeURLSettingActivity.class,null);
            }
                break;
            case WalletPasswd:
            {
                BaseActivity.start(getActivity(), PincodeSettingActivity.class,null);
            }
                break;
            case MineProfile:
            {
                // TODO: 2017/10/16
            }
                break;
        }
    }

    public enum Item {
        Language("语言选择", true),
        NodeURL("节点URL", true),
        WalletPasswd("钱包密码", true),
        MineProfile("个人信息", true),
        ;
        public String title;
        public boolean hasArrow;
        Item(String name, boolean hasArrow) {
            this.title = name;
            this.hasArrow = hasArrow;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_app_setting,container,false);
        ButterKnife.bind(this,rootView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),RecyclerView.VERTICAL));
        adapter=new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values()),simple_list_item_2,this) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                holder.text(android.R.id.text1,model.title);
                holder.text(android.R.id.text2,model.title);
                holder.textColorId(android.R.id.text2, R.color.colorAccent);
            }
        };
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setPresenter(AppSettingContract.Presenter presenter) {

    }

    @Override
    public void displaySettings(List<SettingItem> items) {

    }
}
