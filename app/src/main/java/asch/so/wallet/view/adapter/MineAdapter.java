package asch.so.wallet.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import asch.so.wallet.R;
import asch.so.wallet.view.entity.MineItem;
import asch.so.wallet.view.entity.MineSection;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/28.
 */

public class MineAdapter extends BaseSectionQuickAdapter<MineSection,MineAdapter.ViewHolder> {

    public MineAdapter(List<MineSection> data) {
        super(R.layout.item_mine, R.layout.mine_section_head, data);
    }

    @Override
    protected void convertHead(ViewHolder viewHolder, MineSection mineSection) {

    }

    @Override
    protected void convert(ViewHolder viewHolder, MineSection mineSection) {
        MineItem item=mineSection.t;
        viewHolder.titleTv.setText(item.getTitle());
        int postion = viewHolder.getLayoutPosition();
        switch (postion){
            case 0:{

            }
            break;
            case 1:{

            }
            break;
            case 2:{

            }
            break;
            case 3:{

            }
            break;
            case 4:{

            }
            break;
            case 5:{

            }
            break;
        }

    }


    static class  ViewHolder extends BaseViewHolder{

        @BindView(R.id.item_title_tv)
        TextView titleTv;
        @BindView(R.id.item_sub_title_tv)
        TextView subTitleTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
           // itemView.setOnClickListener(v->adapter.onItemHolderClick(this));
        }
    }
}
