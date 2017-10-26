package asch.so.wallet.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.widget.itenticon.SymmetricIdenticon;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountsAdapter extends BaseQuickAdapter<Account, AccountsAdapter.ViewHolder> {

    public AccountsAdapter() {
        super(R.layout.item_account);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Account account) {
        viewHolder.identicon.show(account.getAddress());
        viewHolder.nameTv.setText(account.getName());
        viewHolder.addressTv.setText(account.getAddress());
        if (account.equals(AccountsManager.getInstance().getCurrentAccount())){
            viewHolder.checkmarkIv.setVisibility(View.VISIBLE);
        }else {
            viewHolder.checkmarkIv.setVisibility(View.INVISIBLE);
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.ident_icon)
        SymmetricIdenticon identicon;

        @BindView(R.id.item_tv_name)
        TextView nameTv;

        @BindView(R.id.address_tv)
        TextView addressTv;

        @BindView(R.id.checkmark)
        ImageView checkmarkIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            //itemView.setOnClickListener(v->adapter.onItemHolderClick(ViewHolder.this));
        }
    }
}
