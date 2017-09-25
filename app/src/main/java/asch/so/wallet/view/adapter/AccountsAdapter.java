package asch.so.wallet.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Account;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountsAdapter extends BaseRecyclerViewAdapter<AccountsAdapter.ViewHolder>{

    private final List<Account> accountList;
    public AccountsAdapter(List<Account>  accounts){
        this.accountList=accounts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account,parent,false);

        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Account account=accountList.get(position);
        holder.nameTv.setText(account.getName());
        //todo
    }

    @Override
    public int getItemCount() {
        return accountList==null?0:accountList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_tv_name)
        TextView nameTv;

        public ViewHolder(View itemView, AccountsAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v->adapter.onItemHolderClick(ViewHolder.this));
        }
    }
}
