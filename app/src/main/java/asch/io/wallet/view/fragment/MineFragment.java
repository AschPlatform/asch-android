package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import asch.io.base.activity.BaseActivity;
import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.activity.AboutActivity;
import asch.io.wallet.activity.AccountInfoActivity;
import asch.io.wallet.activity.AccountsActivity;
import asch.io.wallet.activity.AppSettingActivity;
import asch.io.wallet.activity.BlockBrowseActivity;
import asch.io.wallet.activity.BlockInfoActivity;
import asch.io.wallet.activity.EditAccountNicknameActivity;
import asch.io.wallet.activity.PeersActivity;
import asch.io.wallet.activity.SecureSettingActivity;
import asch.io.wallet.activity.TodoActivity;
import asch.io.wallet.activity.TransactionsActivity;
import asch.io.wallet.activity.VoteActivity;
import asch.io.wallet.activity.WebActivity;
import asch.io.wallet.contract.MineContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.presenter.MinePresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.util.IdenticonGenerator;
import asch.io.wallet.view.adapter.MineAdapter;
import asch.io.wallet.view.entity.MineItem;
import asch.io.wallet.view.entity.MineSection;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class MineFragment extends BaseFragment implements MineContract.View,View.OnClickListener {
    private  static final  String TAG=MineFragment.class.getSimpleName();
    MineContract.Presenter presenter;
    @BindView(R.id.mine_rcv)
    RecyclerView mineRcv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.ident_icon)
    ImageView identicon;
    @BindView(R.id.copy_address)
    View copyView;
    Unbinder unbinder;

    private List<MineSection> itemList=new ArrayList<>();
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
        unbinder = ButterKnife.bind(this, rootView);

        copyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyAddress();
            }
        });
        identicon.setOnClickListener(this);
        nameTv.setOnClickListener(this);
        mineRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        mineRcv.setItemAnimator(new DefaultItemAnimator());
        mineRcv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        mineRcv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                MineSection section =(MineSection) baseQuickAdapter.getItem(position);
                if (!section.isHeader){
                    MineItem item=section.t;
                    switch (item.getIcon()){
                        //账户设置
                        case R.mipmap.icon_anto:
                        {
                            Intent intent = new Intent(getActivity(), AccountsActivity.class);
                            startActivity(intent);
                        }
                        break;

                        //安全设置
                        case R.mipmap.icon_safe:{
                            Intent intent = new Intent(getActivity(), SecureSettingActivity.class);
                            startActivity(intent);
                        }
                        break;

                        //设置
                        case  R.mipmap.icon_shez:
                        {
                            BaseActivity.start(getActivity(),AppSettingActivity.class,null);

                        }
                        break;

                        //关于我们
                        case  R.mipmap.icon_shiy:
                        {
                            BaseActivity.start(getActivity(),WebActivity.class,null);
                        }
                        break;

                        //关于我们
                        case  R.mipmap.icon_myuser:
                        {
                            BaseActivity.start(getActivity(),AboutActivity.class,null);
                        }
                        break;

                        //##################### INVISIBLE #####################

                        case R.mipmap.my_bill:
                        {
                            BaseActivity.start(getActivity(), TransactionsActivity.class, new Bundle());
                        }
                        break;

                        case R.mipmap.personal_center:
                        {
                            BaseActivity.start(getActivity(),AccountInfoActivity.class,null);
                        }
                        break;

                        case R.mipmap.node_vote:
                        {
                            BaseActivity.start(getActivity(),VoteActivity.class,null);
                        }
                        break;

                        case  R.mipmap.vote_list:
                        {
                            BaseActivity.start(getActivity(),PeersActivity.class,null);
                        }
                        break;
                        case  R.mipmap.my_block_info:
                        {
                            BaseActivity.start(getActivity(),BlockInfoActivity.class,null);
                        }
                        break;
                        case  R.mipmap.my_contacts:
                        {
                            BaseActivity.start(getActivity(),BlockBrowseActivity.class,null);
                        }
                        break;



                        default:
                        {
                            BaseActivity.start(getActivity(), TodoActivity.class,null);
                        }
                        break;
                    }
                }

            }
        });
        if (presenter==null){
            presenter=new MinePresenter(getContext(), this);
        }
        presenter.loadAccount();
        presenter.loadItems();
        return rootView;
    }

    private void copyAddress(){
        String address=((TextView)copyView.findViewById(R.id.address_tv)).getText().toString().trim();
        if (!TextUtils.isEmpty(address)){
            AppUtil.copyText(getActivity(),address);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {

    }

    @Override
    public void displayAccount(Account account) {
        nameTv.setText(account.getName());
        addressTv.setText(account.getAddress());
        IdenticonGenerator.getInstance().generateBitmap(account.getPublicKey(), new IdenticonGenerator.OnIdenticonGeneratorListener() {
            @Override
            public void onIdenticonGenerated(Bitmap bmp) {

                identicon.setImageBitmap(bmp);
            }
        });
    }

    @Override
    public void displayItems(List<MineSection> items) {
        this.adapter.replaceData(items);
    }

    @Override
    public void onClick(View v) {
        if (v==identicon||v==nameTv){
            Intent intent = new Intent(getActivity(),EditAccountNicknameActivity.class);
            startActivity(intent);
        }
    }
}
