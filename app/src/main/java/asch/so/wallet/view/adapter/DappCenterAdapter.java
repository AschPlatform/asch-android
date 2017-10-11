package asch.so.wallet.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Dapp;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/11.
 */

public class DappCenterAdapter extends BaseRecyclerViewAdapter<DappCenterAdapter.ViewHolder> {

    private List<Dapp> dappList;

    public DappCenterAdapter(List<Dapp> dappList) {
        this.dappList = dappList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dapp_center, parent,false);
        return new DappCenterAdapter.ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dapp dapp =dappList.get(position);
        holder.nametTv.setText(dapp.getName());
        holder.desTv.setText(String.valueOf(dapp.getDescription()));
    }

    @Override
    public int getItemCount() {
        return dappList==null?0:dappList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name_tv)
        TextView nametTv;
        @BindView(R.id.description_tv)
        TextView desTv;

        public ViewHolder(View itemView, DappCenterAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(view->adapter.onItemHolderClick(this));
        }
    }
}
