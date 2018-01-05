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
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.AschConst;

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
        int resId=getResIdFromCode(Transaction.Type.fromCode(transaction.getType()));
        String transactionType=context.getResources().getString(resId);
        viewHolder.amountTv.setText(transactionType);
//        viewHolder.amountTv.setText(Transaction.Type.fromCode(transaction.getType()).getName());
//        String ago=timeAgo.getTimeAgo(transaction.dateFromAschTimestamp());
        CharSequence ago= AppUtil.getRelativeTimeSpanString(context, transaction.dateFromAschTimestamp().getTime());
        viewHolder.dateTv.setText(ago);
    }

    private int getResIdFromCode(Transaction.Type type){
        switch (type){
            case Transfer:
            {
                return R.string.general_transfer;
            }
            case Signature:
            {
                return R.string.set_second_secret;
            }
            case Delegate:
            {
                return R.string.register_delegate;
            }
            case Vote:
            {
                return R.string.vote_transaction;
            }
            case MultiSignature:
            {
                return R.string.multi_signature;
            }
            case Dapp:
            {
                return R.string.dapp_transaction;
            }
            case InTransfer:
            {
                return R.string.in_transfer;
            }
            case OutTransfer:
            {
                return R.string.out_transfer;
            }
            case Store:
            {
                return R.string.store_transaction;
            }
            case UIAIssuer:
            {
                return R.string.uia_issuer;
            }
            case UIAAsset:
            {
                return R.string.uia_asset;
            }
            case UIAFlags:
            {
                return R.string.uia_flags;
            }
            case UIA_ACL:
            {
                return R.string.uia_acl;
            }
            case UIAIssue:
            {
                return R.string.uia_issue_asset;
            }
            case UIATransfer:
            {
                return R.string.uia_transfer;
            }
            case Lock:
            {
                return R.string.lock_transaction;
            }
            default:
                break;

        }
        return 0;
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
