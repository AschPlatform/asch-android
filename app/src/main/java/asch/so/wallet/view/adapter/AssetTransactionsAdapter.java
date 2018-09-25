package asch.so.wallet.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Nullable;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.base.util.TimeAgo;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Deposit;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.UIATransferAsset;
import asch.so.wallet.model.entity.Withdraw;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/10/13.
 */

public class AssetTransactionsAdapter extends BaseQuickAdapter<Object, AssetTransactionsAdapter.ViewHolder> {

    //private TimeAgo timeAgo=null;
    private Context context;

    public AssetTransactionsAdapter(Context ctx) {
        super(R.layout.item_transaction);
        context=ctx;
        //timeAgo=new TimeAgo().locale(ctx).with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    protected void convert(ViewHolder viewHolder, Object object) {
        if (object instanceof Transaction){
            Transaction transaction = (Transaction)object;
            viewHolder.transactionTv.setText(transaction.getId());
            boolean isSender=getAccount().getAddress().equals(transaction.getSenderId());
            setTransferIcon(viewHolder.transferIcon,transaction.getType(),isSender);
            // int resId=AppUtil.getResIdFromCode(Transaction.Type.fromCode(transaction.getType()));
            //String transactionType=context.getResources().getString(resId);
            //viewHolder.amountTv.setText(transactionType);
            viewHolder.amountTv.setText(transaction.getBanlanceShow(isSender));
            CharSequence ago= AppUtil.getRelativeTimeSpanString(context, transaction.dateFromAschTimestamp().getTime());
            viewHolder.dateTv.setText(ago);
            if (transaction.getFee()!=0){
                viewHolder.transationFee.setText(R.string.transaction_fee);
            }
        }else if(object instanceof Deposit){
            Deposit deposit = (Deposit)object;
            viewHolder.transactionTv.setText(deposit.getAddress());
            viewHolder.transferIcon.setImageResource(R.mipmap.icon_recharge);

            viewHolder.amountTv.setText("+ "+deposit.getBanlanceShow());
            CharSequence ago= AppUtil.getRelativeTimeSpanString(context, deposit.dateFromAschTimestamp().getTime());
            viewHolder.dateTv.setText(ago);
            viewHolder.transationFee.setText(R.string.sure_time+":"+String.valueOf(deposit.getConfirmations())+R.string.done);


        }else if(object instanceof Withdraw){
            Withdraw withdraw = (Withdraw)object;
            viewHolder.transactionTv.setText(withdraw.getRecipientId());
            viewHolder.transferIcon.setImageResource(R.mipmap.icon_withdraw);

            viewHolder.amountTv.setText("- "+withdraw.getBanlanceShow());
            CharSequence ago= AppUtil.getRelativeTimeSpanString(context, withdraw.dateFromAschTimestamp().getTime());
            viewHolder.dateTv.setText(ago);
            viewHolder.transationFee.setText(R.string.sure_time);
        }

    }



    private void setTransferIcon(ImageView imageView, int type, boolean isSender){
        if ( TransactionType.basic_transfer.getCode()==type || TransactionType.UIATransferV2.getCode()==type){
            imageView.setImageResource(isSender?R.mipmap.icon_transfer_accounts:R.mipmap.icon_receivables);
            return;
        }
        imageView.setImageResource(R.mipmap.transfer_other_icon);
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.transaction_fee)
        TextView transationFee;
        @BindView(R.id.transfer_icon)
        ImageView transferIcon;
        @BindView(R.id.transactionid_tv)
        TextView transactionTv;
        @BindView(R.id.amount_tv)
        TextView amountTv;
        @BindView(R.id.date_tv)
        TextView dateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
