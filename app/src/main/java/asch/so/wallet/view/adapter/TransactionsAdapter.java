package asch.so.wallet.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.so.wallet.R;
import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.UIATransferAsset;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/10/18.
 */

public class TransactionsAdapter extends BaseQuickAdapter<Transaction, TransactionsAdapter.ViewHolder> {

    public TransactionsAdapter() {

        super(R.layout.item_transaction);
    }

    @Override
    protected void convert(TransactionsAdapter.ViewHolder viewHolder, Transaction transaction) {
        viewHolder.transactionTv.setText(transaction.getId());
        if ( TransactionType.Transfer.getCode()==transaction.getType()){
            viewHolder.amountTv.setText("普通转账");
            //viewHolder.amountTv.setText(String.format("%.3f",transaction.getAmount()/ (double) AschConst.COIN)+" XAS");
        }else if (TransactionType.UIATransfer.getCode()==transaction.getType()){
            viewHolder.amountTv.setText("自定义资产转账");
//            UIATransferAsset asset=(UIATransferAsset)transaction.getAssetInfo();
//            viewHolder.amountTv.setText(asset.getUiaTransfer().getAmountShow()+" "+asset.getUiaTransfer().getCurrency());
        }else {
            viewHolder.amountTv.setText("其他转账类型");
        }
    }

    public static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.transactionid_tv)
        TextView transactionTv;
        @BindView(R.id.amount_tv)
        TextView amountTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
//            transactionTv=itemView.findViewById(R.id.transactionid_tv);
//            amountTv=itemView.findViewById(R.id.ammount_tv);
        }
    }

}
