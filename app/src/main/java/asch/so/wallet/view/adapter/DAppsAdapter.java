package asch.so.wallet.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.so.wallet.R;
import asch.so.wallet.model.entity.Dapp;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2018/1/19.
 */

public class DAppsAdapter extends BaseQuickAdapter<Dapp, DAppsAdapter.ViewHolder> {
    private Context context;

    public DAppsAdapter(Context context) {
        super(R.layout.item_dapps);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder helper, Dapp item) {
        helper.nameTv.setText(item.getName());
        helper.descriptionTv.setText(item.getName());
    }



    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.description_tv)
        TextView descriptionTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
