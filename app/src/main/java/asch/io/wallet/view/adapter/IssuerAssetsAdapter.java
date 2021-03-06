package asch.io.wallet.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.io.wallet.R;
import asch.io.wallet.model.entity.IssuerAssets;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class IssuerAssetsAdapter extends BaseQuickAdapter<IssuerAssets, IssuerAssetsAdapter.ViewHolder>  {

    Context context;
    public IssuerAssetsAdapter(Context ctx) {
        super(R.layout.item_issue_asset);
        context = ctx;
    }

    @Override
    protected void convert(ViewHolder viewHolder, IssuerAssets assets) {

        String name = assets.getName();
//        String subName = "";
//        if (name!=null&&name.contains(".")){
//            subName = name.substring(name.indexOf(".")+1);
//            name = name.substring(0,name.indexOf("."));
//        }
        viewHolder.name_main.setText(name==null?"":name);
//        viewHolder.name_minor.setText(subName);
        if (assets.getPrecision()!=0&&!TextUtils.isEmpty(assets.getMaximum())&&!TextUtils.isEmpty(assets.getQuantity())){
            viewHolder.precision_tv.setText(String.valueOf(assets.getPrecision())==null?"":String.valueOf(assets.getPrecision()));
            String max = assets.getMaximum();
            int p = assets.getPrecision();
            max = AppUtil.getStringFromBigAmount(max,p);
            String amount =assets.getQuantity();
            amount = AppUtil.getStringFromBigAmount(amount,p);
            viewHolder.max_issue_tv.setText(max);
            viewHolder.amount_tv.setText(amount);
            CharSequence time=  AppUtil.getDateTimeFromTimestamp(context, assets.dateFromAschTimestamp().getTime());
            viewHolder.issuer_time_tv.setText(time);
        }

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.asset_name_tv)
        TextView name_main;

        @BindView(R.id.amount_tv)
        TextView amount_tv;

        @BindView(R.id.precision_tv)
        TextView precision_tv;

        @BindView(R.id.max_issue_tv)
        TextView max_issue_tv;

        @BindView(R.id.issuer_time_tv)
        TextView issuer_time_tv;

        @BindView(R.id.asset_issue_tv)
        TextView asset_issue_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
