package asch.so.wallet.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.model.entity.Transaction;

/**
 * Created by kimziv on 2017/10/18.
 */

public class TransactionsAdapter extends BaseQuickAdapter<Transaction, BaseViewHolder> {

    public TransactionsAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Transaction transaction) {

    }

}
