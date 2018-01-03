package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.activity.AboutActivity;
import asch.so.wallet.activity.AccountsActivity;
import asch.so.wallet.activity.AppSettingActivity;
import asch.so.wallet.activity.BlockInfoActivity;
import asch.so.wallet.activity.PeersActivity;
import asch.so.wallet.activity.TodoActivity;
import asch.so.wallet.activity.VoteActivity;
import asch.so.wallet.activity.WebActivity;
import asch.so.wallet.contract.MineContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.MinePresenter;
import asch.so.wallet.util.IdenticonGenerator;
import asch.so.wallet.view.adapter.MineAdapter;
import asch.so.wallet.view.entity.MineItem;
import asch.so.wallet.view.entity.MineSection;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class MineFragment extends BaseFragment implements MineContract.View{
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
//    @BindView(R.id.app_bar_mine)
//    AppBarLayout appBarLayout;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;



    public enum Item{
       AccountManagement("","账户管理",true),
        AppSetting("","设置",true),
        Contacts("","联系人",true),
        BlockInfo("","区块详情",true),
        About("","关于",true);

        public String icon;
        public String title;
        public boolean hasArrow;

        Item(String icon, String title, boolean hasArrow) {
            this.icon = icon;
            this.title = title;
            this.hasArrow = hasArrow;
        }
    }

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
        ButterKnife.bind(this,rootView);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean misAppbarExpand = true;
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
//                int scrollRange = appBarLayout.getTotalScrollRange();
//                Log.v(TAG,"verticalOffset:"+verticalOffset+", scrollRange:"+scrollRange);
//                float fraction = 1f * (scrollRange + verticalOffset) / scrollRange;
//                toolbar.setAlpha((1-fraction));
//                toolbar.setBackgroundResource(R.mipmap.toolbar);
//
//                if (fraction < 0.1 && misAppbarExpand) {
//                    misAppbarExpand = false;
//                    //addIconIv.setAlpha(1.0f);
//                    //topBalanceTv.setAlpha(1.0f);
//                }
//                if (fraction > 0.8 && !misAppbarExpand) {
//                    misAppbarExpand = true;
//                    // addIconIv.setAlpha(0);
//                    //topBalanceTv.setAlpha(0);
//                }
//            }
//        });

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
                        case R.mipmap.my_account_managment:
                        {
                            Intent intent = new Intent(getActivity(), AccountsActivity.class);
                            startActivity(intent);
                        }
                        break;
                        case R.mipmap.my_settings:
                        {
                            BaseActivity.start(getActivity(),AppSettingActivity.class,null);
                        }
                        break;
                        case R.mipmap.my_users:
                        {
                            BaseActivity.start(getActivity(),AboutActivity.class,null);
                        }
                        break;
                        case  R.mipmap.my_user:
                        {
                            BaseActivity.start(getActivity(),WebActivity.class,null);
                        }
                        break;
                        case  R.mipmap.my_bell:
                        {
                            BaseActivity.start(getActivity(),VoteActivity.class,null);
                        }
                        break;
                        case  R.mipmap.my_block_info:
                        {
                            BaseActivity.start(getActivity(),BlockInfoActivity.class,null);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
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
}
