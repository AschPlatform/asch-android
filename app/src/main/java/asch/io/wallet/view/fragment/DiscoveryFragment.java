package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import asch.io.base.activity.BaseActivity;
import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.activity.BlockBrowseActivity;
import asch.io.wallet.activity.BlockInfoActivity;
import asch.io.wallet.activity.IssuerAssetsActivity;
import asch.io.wallet.activity.PeersActivity;
import asch.io.wallet.contract.DiscoveryContract;
import asch.io.wallet.presenter.DiscoveryPresenter;
import asch.io.wallet.view.adapter.MineAdapter;
import asch.io.wallet.view.entity.MineItem;
import asch.io.wallet.view.entity.MineSection;
import asch.io.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2018/7/17.
 */

public class DiscoveryFragment extends BaseFragment implements DiscoveryContract.View{
    private  static final  String TAG=DiscoveryFragment.class.getSimpleName();
    DiscoveryContract.Presenter presenter;
   private TitleToolbar toolbar=null;
    @BindView(R.id.discovery_rcv)
    RecyclerView discovery_rcv;
    private List<MineSection> itemList=new ArrayList<>();
    private MineAdapter adapter =new MineAdapter(itemList);
    public static DiscoveryFragment newInstance() {

        Bundle args = new Bundle();
        DiscoveryFragment fragment = new DiscoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_discovery,container,false);
        ButterKnife.bind(this,rootView);
        initToolBar(rootView);

        discovery_rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        discovery_rcv.setItemAnimator(new DefaultItemAnimator());
        discovery_rcv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        discovery_rcv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                MineSection section =(MineSection) baseQuickAdapter.getItem(position);
                if (!section.isHeader){
                    MineItem item=section.t;
                    switch (item.getIcon()){
                        case R.mipmap.icon_mymoney:
                        {
                            BaseActivity.start(getActivity(),IssuerAssetsActivity.class,null);
                        }
                        case R.mipmap.node_vote:
                        {
//                            BaseActivity.start(getActivity(),IssuerAssetsActivity.class,null);
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

                    }
                }

            }
        });
        if (presenter==null) {
            presenter = new DiscoveryPresenter(getContext(), this);
        }
        presenter.loadItems();
        return rootView;
    }

    protected void initToolBar(View root){
        toolbar = (TitleToolbar) root.findViewById(R.id.toolbar);
        toolbar.setBackVisible(false);
        toolbar.setRightVisible(false);
        toolbar.setCloseVisible(false);
        toolbar.setTitle(getString(R.string.Discovery));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
    }

    @Override
    public void setPresenter(DiscoveryContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {

    }


    @Override
    public void displayItems(List<MineSection> items) {
        this.adapter.replaceData(items);
    }
}
