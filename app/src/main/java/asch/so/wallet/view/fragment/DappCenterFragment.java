package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.UIException;
import asch.so.wallet.R;
import asch.so.wallet.activity.DappActivity;
import asch.so.wallet.contract.DappCenterContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.view.adapter.AssetsAdapter;
import asch.so.wallet.view.adapter.DappCenterAdapter;
import asch.so.wallet.view.adapter.DappsCenterAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by kimziv on 2017/10/11.
 */

public class DappCenterFragment extends BaseFragment implements DappCenterContract.View{
    private static final String TAG = DappCenterFragment.class.getSimpleName();

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.dapp_list_rv)
    RecyclerView dappListRv;

    DappCenterContract.Presenter presenter;

   // private List<Dapp> dappList=new ArrayList<>();
    private DappsCenterAdapter adapter=new DappsCenterAdapter();


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
        dappListRv.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        dappListRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                Intent intent=new Intent(getActivity(),DappActivity.class);
                startActivity(intent);
            }
        });
//        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
//                Intent intent=new Intent(getActivity(), DappActivity.class);
//                startActivity(intent);
//               // deposit(TestData.dappID,"dujunhui.ZICHAN",100000003,null,TestData.secret,null);
//            }
//        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() < 2) {
                           presenter.loadDappList();
                        }
                        refreshlayout.finishRefresh();
                    }
                },2000);
            }
        });


        //添加Header
        View header = LayoutInflater.from(getContext()).inflate(R.layout.banner_dapp_center, dappListRv, false);
        Banner banner = (Banner) header;
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(BANNER_ITEMS);
        banner.start();
        adapter.addHeaderView(banner);

        return rootView;
    }

    public static List<BannerItem> BANNER_ITEMS = new ArrayList<BannerItem>(){{
        add(new BannerItem("CCTime", R.mipmap.mine_header_bg));
        add(new BannerItem("孔明屋", R.mipmap.mine_header_bg));
        add(new BannerItem("受托人投票", R.mipmap.mine_header_bg));
    }};

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setImageResource(((BannerItem) path).pic);
        }
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
    public void displayError(UIException exception) {

    }

    @Override
    public void displayDappList(List<Dapp> dapps) {
//        this.dappList.clear();
//        this.dappList.addAll(dapps);
        adapter.replaceData(dapps);
//        adapter.notifyDataSetChanged();
    }

    public static class BannerItem {

        public int pic;
        public String title;

        public BannerItem() {
        }

        public BannerItem(String title, int pic) {
            this.pic = pic;
            this.title = title;
        }
    }
}
