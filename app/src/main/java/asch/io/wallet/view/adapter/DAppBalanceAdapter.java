package asch.io.wallet.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.model.entity.DAppBalance;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2018/2/9.
 */

public class DAppBalanceAdapter extends BaseQuickAdapter<DAppBalance,DAppBalanceAdapter.ViewHolder> {

    private Context context;

    public DAppBalanceAdapter(Context context) {
        super(R.layout.item_dapp_balance);
        this.context=context;
    }

    @Override
    protected void convert(ViewHolder viewHolder, DAppBalance item) {
        viewHolder.currencyTv.setText(item.getCurrency());

        if (AppConstants.XAS_NAME.equals(item.getCurrency())){
            viewHolder.circulationTv.setText(String.valueOf(AppConstants.XAS_CIRCULATION));
            item.setPrecision(AppConstants.PRECISION);
           viewHolder.balanceTv.setText(item.getBalanceString());
        }else{
            viewHolder.circulationTv.setText(item.getQuantityShow());
            viewHolder.balanceTv.setText(item.getBalanceShow());
        }
    }

    public static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.currency_tv)
        TextView currencyTv;
        @BindView(R.id.circulation_tv)
        TextView circulationTv;
        @BindView(R.id.balance_tv)
        TextView balanceTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
