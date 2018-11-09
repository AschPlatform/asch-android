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
import butterknife.BindView;
import butterknife.ButterKnife;



public class AssetManageAdapter extends BaseRecyclerViewAdapter<AssetManageAdapter.ViewHolder> {


    private final List<AschAsset> assetList;

    public AssetManageAdapter(List<AschAsset>  assets){
        this.assetList=assets;
    }

    @Override
    public AssetManageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset_manage, parent,false);
        return new AssetManageAdapter.ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(AssetManageAdapter.ViewHolder viewHolder, int position) {
        AschAsset assets = assetList.get(position);
        viewHolder.name.setText(assets.getName());
        if (assets.getName().equals( AppConstants.XAS_NAME)){
            viewHolder.icon.setImageResource(R.mipmap.icon_xas);
            viewHolder.checkmarkIv.setImageResource(R.mipmap.icon_check_gray);
        }else if (assets.getType()==AschAsset.TYPE_UIA){
            viewHolder.icon.setImageResource(R.mipmap.icon_uia);
        }else if (assets.getType()==AschAsset.TYPE_GATEWAY){
            viewHolder.icon.setImageResource(R.mipmap.icon_gateway);
        }

        if(!assets.getName().equals(AppConstants.XAS_NAME)){
            if (assets.getShowState()==AschAsset.STATE_SHOW
                    ){
                viewHolder.checkmarkIv.setImageResource(R.mipmap.icon_check);
            }else {
                viewHolder.checkmarkIv.setImageResource(R.mipmap.icon_uncheck);
            }
        }
    }

    @Override
    public int getItemCount() {
        return assetList==null?0:assetList.size();
    }
    static class  ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_assets_manage_coin_img)
        ImageView icon;

        @BindView(R.id.item_assets_manage_coin_name)
        TextView name;

        @BindView(R.id.item_assets_manage_check)
        ImageView checkmarkIv;

        public ViewHolder(View itemView, AssetManageAdapter adapter){
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view -> adapter.onItemHolderClick(this));
        }
    }



}
