package asch.io.wallet.view.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.util.IdenticonGenerator;
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

        viewHolder.nameTv.setText(account.getName());
        viewHolder.addressTv.setText(account.getAddress());
        if (account.equals(AccountsManager.getInstance().getCurrentAccount())){
            viewHolder.checkmarkIv.setVisibility(View.VISIBLE);
        }else {
            viewHolder.checkmarkIv.setVisibility(View.INVISIBLE);
        }

        IdenticonGenerator.getInstance().generateBitmap(account.getPublicKey(), new IdenticonGenerator.OnIdenticonGeneratorListener() {
            @Override
            public void onIdenticonGenerated(Bitmap bmp) {
                viewHolder.identicon.setImageBitmap(bmp);
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.ident_icon)
        ImageView identicon;

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
