package asch.io.wallet.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.Transaction;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.TransactionType;

/**
 * Created by kimziv on 2017/10/18.
 */

public class TransactionsAdapter extends BaseQuickAdapter<Transaction, TransactionsAdapter.ViewHolder> {

   // private TimeAgo timeAgo=null;
    private Context context;
    public TransactionsAdapter(Context ctx) {
        super(R.layout.item_transaction);
        this.context=ctx;
       // timeAgo=new TimeAgo().locale(ctx).with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    protected void convert(ViewHolder viewHolder, Transaction transaction) {
        boolean isSender=getAccount().getAddress().equals(transaction.getSenderId());
        setTransferIcon(viewHolder.transferIcon,transaction.getType(),isSender);
        viewHolder.transactionTv.setText(transaction.getId());
        int resId=AppUtil.getResIdFromCode(Transaction.Type.fromCode(transaction.getType()));
        String transactionType=context.getResources().getString(resId);
        viewHolder.amountTv.setText(transactionType);
//        viewHolder.amountTv.setText(Transaction.Type.fromCode(transaction.getType()).getName());
//        String ago=timeAgo.getTimeAgo(transaction.dateFromAschTimestamp());
        CharSequence ago= AppUtil.getRelativeTimeSpanString(context, transaction.dateFromAschTimestamp().getTime());
        viewHolder.dateTv.setText(ago);
    }


    private void setTransferIcon(ImageView imageView, int type, boolean isSender){
        if ( TransactionType.basic_transfer.getCode()==type || TransactionType.UIATransferV2.getCode()==type){
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
