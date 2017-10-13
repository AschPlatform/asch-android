package asch.so.wallet.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Transaction;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/13.
 */

public class AssetTransactionsAdapter extends BaseRecyclerViewAdapter<AssetTransactionsAdapter.ViewHolder>{

    private List<Transaction> txList;

    public AssetTransactionsAdapter(List<Transaction> txList) {
        this.txList = txList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction,parent,false);

        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.transactionTv.setText("");
//        holder.amountTv.setText("");
    }

    @Override
    public int getItemCount() {
        return txList==null?0:txList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.transaction_id_tv)
        TextView transactionTv;
        @BindView(R.id.amount_tv)
        TextView amountTv;

        public ViewHolder(View itemView, AssetTransactionsAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view -> adapter.onItemHolderClick(this));
        }
    }
}
