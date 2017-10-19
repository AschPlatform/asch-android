package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.AccountsActivity;
import asch.so.wallet.activity.AssetTransactionsActivity;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.contract.AssetReceiveContract;
import asch.so.wallet.contract.AssetsContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.adapter.AccountsAdapter;
import asch.so.wallet.view.adapter.AssetsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AssetsFragment extends BaseFragment implements AssetsContract.View, View.OnClickListener{

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.assets_rcv)
    RecyclerView assetsRcv;
    @BindView(R.id.app_bar)
   AppBarLayout appBarLayout;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.add_icon)
     ImageView addIconIv;
    Unbinder unbinder;

    private int mOffset = 0;
    private int mScrollY = 0;

    private List<Balance> assetList=new ArrayList<>();
    private AssetsAdapter adapter=new AssetsAdapter(assetList);

    private AssetsContract.Presenter presenter;

    public static AssetsFragment newInstance() {

        Bundle args = new Bundle();

        AssetsFragment fragment = new AssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_assets,container,false);
        unbinder = ButterKnife.bind(this,rootView);
        assetsRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        assetsRcv.setItemAnimator(new DefaultItemAnimator());
        assetsRcv.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        assetsRcv.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
                Balance balance = assetList.get(position);
                Intent intent = new Intent(getActivity(), AssetTransactionsActivity.class);
                intent.putExtra("curreny",balance.getCurrency());
                intent.putExtra("precision",balance.getPrecision());
                startActivity(intent);
            }
        });
        addIconIv.setOnClickListener(this);

        setupRefreshLayout();

        return rootView;
    }


    private void setupRefreshLayout(){

//        //状态栏透明和间距处理
//        StatusBarUtil.immersive(getActivity());
//        StatusBarUtil.setPaddingSmart(getActivity(), toolbar);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                // TODO: 2017/10/13
                refreshlayout.finishRefresh(2000);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                // TODO: 2017/10/13
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean misAppbarExpand = true;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                float fraction = 1f * (scrollRange + verticalOffset) / scrollRange;
                if (fraction < 0.1 && misAppbarExpand) {
                    misAppbarExpand = false;
                    floatingActionButton.animate().scaleX(0).scaleY(0);
                }
                if (fraction > 0.8 && !misAppbarExpand) {
                    misAppbarExpand = true;
                    floatingActionButton.animate().scaleX(1).scaleY(1);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==addIconIv){
            PopupMenu popupMenu =new PopupMenu(getActivity(),addIconIv, Gravity.END);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.item_scan:
                        {

                        }
                        break;
                        case R.id.item_receive:
                        {

                        }
                        break;
                        case R.id.item_transactions:
                        {

                        }
                        break;
                    }
                    return false;
                }
            });

            MenuPopupHelper menuHelper = new MenuPopupHelper(getContext(), (MenuBuilder)popupMenu.getMenu(), addIconIv);
            menuHelper.setForceShowIcon(true);
            menuHelper.show();

            //popupMenu.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(AssetsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayAssets(List<Balance> assetList) {
            this.assetList.clear();
            this.assetList.addAll(assetList);
            adapter.notifyDataSetChanged();
    }
}
