package asch.so.wallet.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.base.util.TimeAgo;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
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

    private TimeAgo timeAgo=null;
    private Context context;

    public AssetTransactionsAdapter(Context ctx) {
        super(R.layout.item_transaction);
        context=ctx;
        timeAgo=new TimeAgo().locale(ctx).with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    protected void convert(ViewHolder viewHolder, Transaction transaction) {
        viewHolder.transactionTv.setText(transaction.getId());
        boolean isSender=getAccount().getAddress().equals(transaction.getSenderId());
       if ( TransactionType.Transfer.getCode()==transaction.getType()){
           viewHolder.amountTv.setText(String.format("%s%.3f", isSender?"-":"+",transaction.getAmount()/ (double)AschConst.COIN)+" XAS");

       }else if (TransactionType.UIATransfer.getCode()==transaction.getType()){
           UIATransferAsset asset=(UIATransferAsset)transaction.getAssetInfo();
           viewHolder.amountTv.setText(String.format("%s%.3f", isSender?"-":"+",Float.parseFloat(asset.getUiaTransfer().getAmountShow()))+" "+asset.getUiaTransfer().getCurrency());

       }

        String ago=timeAgo.getTimeAgo(transaction.dateFromAschTimestamp());
        viewHolder.dateTv.setText(ago);
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.transactionid_tv)
        TextView transactionTv;
        @BindView(R.id.amount_tv)
        TextView amountTv;
        @BindView(R.id.date_tv)
        TextView dateTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
//            transactionTv=itemView.findViewById(R.id.transactionid_tv);
//            amountTv=itemView.findViewById(R.id.ammount_tv);
        }
    }
}
