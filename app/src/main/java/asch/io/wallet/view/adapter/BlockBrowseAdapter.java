package asch.io.wallet.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import asch.io.wallet.R;
import asch.io.wallet.model.entity.Block;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.AschSDK;

/**
 * Created by haizeiwang on 2018/03/14.
 *区块浏览
 */
public class BlockBrowseAdapter extends BaseQuickAdapter<Block, BlockBrowseAdapter.ViewHolder> {

    private  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public BlockBrowseAdapter(@Nullable List<Block> data) {
        super(R.layout.item_blockbrowse, data);
    }

    public BlockBrowseAdapter() {
        this(null);
    }


    @Override
    protected void convert(ViewHolder viewHolder, Block block) {
        viewHolder.height.setText(block.getHeight()+"");
        Date date = AschSDK.Helper.dateFromAschTimestamp(block.getTimestamp());
        String dateTime = TimeUtils.date2String(date,sdf);
        viewHolder.date.setText(dateTime);
    }


    public class ViewHolder  extends BaseViewHolder {
        @BindView(R.id.item_height)
        TextView height;
        @BindView(R.id.item_date)
        TextView date;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
