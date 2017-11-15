package asch.so.wallet.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetsAdapter extends BaseRecyclerViewAdapter<AssetsAdapter.ViewHolder>{

    private final List<Balance> assetList;
    public AssetsAdapter(List<Balance>  assets){
        this.assetList=assets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset, parent,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Balance balance =assetList.get(position);
        holder.currencyIconIv.setImageResource(balance.getCurrency().equals(AschConst.CORE_COIN_NAME)?R.mipmap.xas_icon:R.mipmap.other_coin_icon);
        holder.assetNameTv.setText(balance.getCurrency());
        holder.balanceTv.setText(String.valueOf(balance.getRealBalance()));
    }

    @Override
    public int getItemCount() {
        return assetList==null?0:assetList.size();
    }
    static class  ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.currency_icon)
        ImageView currencyIconIv;
        @BindView(R.id.asset_name_tv)
        TextView assetNameTv;

        @BindView(R.id.balance_tv)
        TextView balanceTv;

        public ViewHolder(View itemView, AssetsAdapter adapter){
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(view -> adapter.onItemHolderClick(this));
        }
    }
}
