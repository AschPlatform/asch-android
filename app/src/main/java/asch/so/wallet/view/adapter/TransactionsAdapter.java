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
import butterknife.BindView;
import butterknife.ButterKnife;

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
        viewHolder.amountTv.setText(String.valueOf(transaction.getAmount()));
    }

    public static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.transaction_id_tv)
        TextView transactionTv;
        @BindView(R.id.amount_tv)
        TextView amountTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}
