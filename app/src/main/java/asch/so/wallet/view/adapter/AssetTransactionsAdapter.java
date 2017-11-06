package asch.so.wallet.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.UIATransferAsset;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/10/13.
 */

public class AssetTransactionsAdapter extends BaseQuickAdapter<Transaction, AssetTransactionsAdapter.ViewHolder> {


    public AssetTransactionsAdapter() {
        super(R.layout.item_transaction);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Transaction transaction) {
        viewHolder.transactionTv.setText(transaction.getId());
       if ( TransactionType.Transfer.getCode()==transaction.getType()){
           viewHolder.amountTv.setText(String.format("%.3f",transaction.getAmount()/ (double)AschConst.COIN)+" XAS");
       }else if (TransactionType.UIATransfer.getCode()==transaction.getType()){
           UIATransferAsset asset=(UIATransferAsset)transaction.getAssetInfo();
           viewHolder.amountTv.setText(String.format("%.3f",Float.parseFloat(asset.getUiaTransfer().getAmountShow()))+" "+asset.getUiaTransfer().getCurrency());

//           viewHolder.amountTv.setText(String.format("%.3f",transaction.getAmount()/ (double)AschConst.COIN)+" "+asset.getUiaTransfer().getCurrency());
       }

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.transaction_id_tv)
        TextView transactionTv;
        @BindView(R.id.amount_tv)
        TextView amountTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
           // itemView.setOnClickListener(view -> adapter.onItemHolderClick(this));
        }
    }
}
