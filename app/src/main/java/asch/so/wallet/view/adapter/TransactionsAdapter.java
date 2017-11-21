package asch.so.wallet.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;

import asch.so.base.util.TimeAgo;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
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

    private TimeAgo timeAgo=null;
    private Context context;
    public TransactionsAdapter(Context ctx) {
        super(R.layout.item_transaction);
        this.context=ctx;
        timeAgo=new TimeAgo().locale(ctx).with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    protected void convert(TransactionsAdapter.ViewHolder viewHolder, Transaction transaction) {
        boolean isSender=getAccount().getAddress().equals(transaction.getSenderId());
        setTransferIcon(viewHolder.transferIcon,transaction.getType(),isSender);
        viewHolder.transactionTv.setText(transaction.getId());
        viewHolder.amountTv.setText(Transaction.Type.fromCode(transaction.getType()).getName());
        String ago=timeAgo.getTimeAgo(transaction.dateFromAschTimestamp());
        viewHolder.dateTv.setText(ago);
    }

    private void setTransferIcon(ImageView imageView, int type, boolean isSender){
        if ( TransactionType.Transfer.getCode()==type || TransactionType.UIATransfer.getCode()==type){
            imageView.setImageResource(isSender?R.mipmap.transfer_out_icon:R.mipmap.transfer_in_icon);
            return;
        }
        imageView.setImageResource(R.mipmap.transfer_other_icon);
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }
    public static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.transfer_icon)
        ImageView transferIcon;
        @BindView(R.id.transactionid_tv)
        TextView transactionTv;
        @BindView(R.id.amount_tv)
        TextView amountTv;
        @BindView(R.id.date_tv)
        TextView dateTv;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}
