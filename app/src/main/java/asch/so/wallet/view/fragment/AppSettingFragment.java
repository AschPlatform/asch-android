package asch.so.wallet.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;

import java.util.Arrays;
import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.activity.AppPinActivity;
import asch.so.wallet.activity.LanguagesActivity;
import asch.so.wallet.activity.NodeURLSettingActivity;
import asch.so.wallet.contract.AppSettingContract;
import asch.so.wallet.view.adapter.BaseRecyclerAdapter;
import asch.so.wallet.view.adapter.SmartViewHolder;
import asch.so.wallet.view.entity.SettingItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/16.
 */

public class AppSettingFragment extends BaseFragment implements AppSettingContract.View, AdapterView.OnItemClickListener{

    private AppSettingContract.Presenter presenter;
    private BaseRecyclerAdapter<Item> adapter;
    private static final int REQUEST_CODE_ENABLE = 11;
    private   Switch pinSwith=null;

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
            case Pincode:
            {
               // BaseActivity.start(getActivity(), PincodeSettingActivity.class,null);
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
        Pincode("设置Pin码", true),
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
        adapter=new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values()),R.layout.item_app_setting,this) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                holder.text(R.id.item_title_tv,model.title);
                Switch sw = ButterKnife.findById(holder.itemView,R.id.pin_switch);
                ImageView arrowIv=ButterKnife.findById(holder.itemView,R.id.arrow_iv);

                if (model==Item.Pincode){
                    pinSwith=sw;
                    holder.itemView.setClickable(false);
                    sw.setChecked(LockManager.getInstance().getAppLock().isPasscodeSet());
                    sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                            if (checked){
                                Intent intent = new Intent(getActivity(), AppPinActivity.class);
                                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                                startActivityForResult(intent, REQUEST_CODE_ENABLE);
                            }else {
                                LockManager.getInstance().getAppLock().disableAndRemoveConfiguration();
                            }
                        }
                    });
                    sw.setVisibility(View.VISIBLE);
                    arrowIv.setVisibility(View.GONE);
                }else {
                    holder.itemView.setClickable(true);
                    sw.setVisibility(View.GONE);
                    arrowIv.setVisibility(View.VISIBLE);
                }

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
    public void displayError(java.lang.Throwable exception) {

    }

    @Override
    public void displaySettings(List<SettingItem> items) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_ENABLE==requestCode){
            if (Activity.RESULT_OK==resultCode){
                if (pinSwith!=null){
                    pinSwith.setChecked(true);
                }
            }
            else {
                if (pinSwith!=null){
                    pinSwith.setChecked(false);
                }
            }
        }
    }
}
