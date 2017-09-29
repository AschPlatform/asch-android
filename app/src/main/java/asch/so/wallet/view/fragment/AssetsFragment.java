package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import asch.so.wallet.activity.AssetTransactionsActivity;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.contract.AssetsContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.view.adapter.AccountsAdapter;
import asch.so.wallet.view.adapter.AssetsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AssetsFragment extends BaseFragment implements AssetsContract.View{

    @BindView(R.id.assets_rcv)
    RecyclerView assetsRcv;

    Unbinder unbinder;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_assets,container,false);
        unbinder = ButterKnife.bind(this,rootView);
        assetsRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        assetsRcv.setItemAnimator(new DefaultItemAnimator());
       // assetList=new ArrayList<>();

//        adapter=new AssetsAdapter(assetList);
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
        return rootView;
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
