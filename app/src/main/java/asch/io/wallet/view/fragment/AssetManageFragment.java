package asch.io.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.contract.AssetManageContract;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.model.entity.BaseAsset;
import asch.io.wallet.presenter.AssetManagePresenter;
import asch.io.wallet.view.adapter.AssetManageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class AssetManageFragment extends BaseFragment implements AssetManageContract.View{
    private static final String TAG=AssetManageFragment.class.getSimpleName();

    @BindView(R.id.asset_manage_rcv_main)
    RecyclerView mainRv;
    @BindView(R.id.asset_manage_rcv_gateway)
    RecyclerView gatewayRv;
    @BindView(R.id.asset_manage_rcv_uia)
    RecyclerView uiaRv;
    View rootView;

    private AssetManageAdapter mainAdapter;
    private Unbinder unbinder;
    private AssetManageContract.Presenter presenter;
    private List<AschAsset> allAssets;
    private List<AschAsset> uiaAssets = new ArrayList<>();
    private AssetManageAdapter uiaAdapter = new AssetManageAdapter(uiaAssets);;
    private List<AschAsset> gatewayAssets = new ArrayList<>();
    private AssetManageAdapter gatewayAdapter = new AssetManageAdapter(gatewayAssets);

    public Boolean isModify = false;

    public static AssetManageFragment newInstance() {
        Bundle args = new Bundle();
        AssetManageFragment fragment = new AssetManageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_manage, container, false);
        unbinder= ButterKnife.bind(this,rootView);

        initRecycleView(gatewayRv);
        initRecycleView(uiaRv);
        initRecycleView(mainRv);

        AschAsset xas = new AschAsset();
        xas.setType(BaseAsset.TYPE_XAS);
        xas.setName(AppConstants.XAS_NAME);
        xas.setShowState(AschAsset.STATE_SHOW);
        List<AschAsset>xasList = new ArrayList<AschAsset>();
        xasList.add(xas);

        mainAdapter =new AssetManageAdapter(xasList);
        mainRv.setAdapter(mainAdapter);
        mainAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        uiaAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> baseQuickAdapter, View view, int position,long id) {
                isModify = true;
                AschAsset coin = uiaAssets.get(position);
                int state =  coin.getShowState();
                if (state==AschAsset.STATE_SHOW)
                    AssetManager.getInstance().updateAssetShowState(coin,AschAsset.STATE_UNSHOW);
                else
                    AssetManager.getInstance().updateAssetShowState(coin,AschAsset.STATE_SHOW);
                uiaAssets.set(position,coin);
                uiaAdapter.notifyDataSetChanged();
            }
        });
        uiaRv.setAdapter(uiaAdapter);


        gatewayAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isModify = true;
                AschAsset coin = gatewayAssets.get(position);
                int state =  coin.getShowState();
                if (state==AschAsset.STATE_SHOW)
                    AssetManager.getInstance().updateAssetShowState(coin,AschAsset.STATE_UNSHOW);
                else
                    AssetManager.getInstance().updateAssetShowState(coin,AschAsset.STATE_SHOW);
                gatewayAssets.set(position,coin);
                gatewayAdapter.notifyDataSetChanged();
            }
        });

        gatewayRv.setAdapter(gatewayAdapter);

        if (presenter!=null) {
            presenter.subscribe();
        }
        presenter=new AssetManagePresenter(getContext(),this);
        presenter.loadAllAssets();
        this.rootView = rootView;
        return rootView;
    }



    private void initRecycleView(RecyclerView v){
        v.setLayoutManager(new LinearLayoutManager(getContext()));
        v.setItemAnimator(new DefaultItemAnimator());
        v.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
        presenter.unSubscribe();
    }


    @Override
    public void setPresenter(AssetManagePresenter presenter) {

    }

    @Override
    public void displayError(Throwable exception) {

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.subscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
//        presenter.loadAllAssets();
    }

    @Override
    public void displayUiaAssets(List<AschAsset> assets) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uiaAssets.clear();
                uiaAssets.addAll(assets);
                uiaAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void displayGatewayAssets(List<AschAsset> assets) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gatewayAssets.clear();
                gatewayAssets.addAll(assets);
                gatewayAdapter.notifyDataSetChanged();
            }
    });
    }

}
