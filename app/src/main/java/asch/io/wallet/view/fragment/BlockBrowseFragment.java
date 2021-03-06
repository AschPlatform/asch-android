package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import asch.io.base.activity.BaseActivity;
import asch.io.wallet.R;
import asch.io.wallet.activity.BlockDetailActivity;
import asch.io.wallet.contract.BlockExplorerContract;
import asch.io.wallet.model.entity.Block;
import asch.io.wallet.presenter.BlockExplorerPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.adapter.BlockBrowseAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by haizeiwang on 2018/03/14.
 * 区块浏览
 */
public class BlockBrowseFragment extends Fragment implements BlockExplorerContract.View{
    KProgressHUD hud=null;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    @BindView(R.id.searchEdit)
    SearchView searchView;
    Unbinder unbinder;
    BlockExplorerContract.Presenter presenter;
    BlockBrowseAdapter adapter;

    public BlockBrowseFragment() {
    }

    public static BlockBrowseFragment newInstance() {
        BlockBrowseFragment fragment = new BlockBrowseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block_browse, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter=new BlockExplorerPresenter(getContext(),this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showHUD();
                presenter.searchBlock(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        adapter=new BlockBrowseAdapter();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Block block = (Block) adapter.getItem(position);
                String json = JSON.toJSONString(block);
                Bundle bundle=new Bundle();
                bundle.putString("block",json);
                BaseActivity.start(getActivity(), BlockDetailActivity.class,bundle);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                searchView.setQuery("",false);
                presenter.loadFirstPageBlocks();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                presenter.loadMorePageBlocks();
            }
        });
        refreshLayout.autoRefresh();
        return view;
    }

    public void refreshData(){
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter!=null)
        {
            presenter.unSubscribe();
        }
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(BlockExplorerContract.Presenter presenter) {
        this.presenter = presenter;
    }



    @Override
    public void displayError(Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            AppUtil.toastError(getContext(),AppUtil.extractInfoFromError(getContext(),exception));
        }
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }
        dismissHUD();
    }


    @Override
    public void displayFirstPageBlocks(List<Block> blocks) {
        if (blocks.isEmpty()) {
            loadingLayout.showEmpty();
        }else {
            loadingLayout.showContent();
        }
        adapter.replaceData(blocks);
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }
        dismissHUD();
    }

    @Override
    public void displayMorePageBlocks(List<Block> blocks) {
        adapter.addData(blocks);
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }
        dismissHUD();
    }


    private  void  showHUD(){
        if (hud==null){
            hud = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .show();
        }else if(!hud.isShowing()){
            hud.show();
        }
    }

    private  void  dismissHUD(){
        if (hud!=null){
            hud.dismiss();
        }
    }
}
