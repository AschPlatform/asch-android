package asch.io.wallet.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import asch.io.base.adapter.BaseRecyclerViewAdapter;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetsAdapter extends BaseRecyclerViewAdapter<AssetsAdapter.ViewHolder>{

    private final List<AschAsset> assetList;
    public AssetsAdapter(List<AschAsset>  assets){
        this.assetList=assets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset, parent,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AschAsset balance =assetList.get(position);
        String currency = balance.getName();
        holder.currencyIconIv.setImageResource(AppUtil.getIconIdByName(currency));
        holder.assetNameTv.setText(balance.getName());
        if (balance.getName().equals(AppConstants.XAS_NAME))
            holder.balanceTv.setText(balance.getXasTotal());
        else
            holder.balanceTv.setText(balance.getBalanceString());
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
