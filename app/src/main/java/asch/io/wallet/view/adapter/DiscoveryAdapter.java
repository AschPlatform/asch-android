package asch.io.wallet.view.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import asch.io.wallet.R;
import asch.io.wallet.view.entity.MineItem;
import asch.io.wallet.view.entity.MineSection;

/**
 * Created by kimziv on 2017/9/28.
 */

public class DiscoveryAdapter extends BaseSectionQuickAdapter<MineSection,BaseViewHolder> {

    public DiscoveryAdapter(List<MineSection> data) {
        super(R.layout.item_mine, R.layout.mine_section_head, data);
    }

    @Override
    protected void convertHead(BaseViewHolder viewHolder, MineSection mineSection) {
//        if (viewHolder.getLayoutPosition()==0){
//            viewHolder.itemView.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, MineSection mineSection) {
        MineItem item=mineSection.t;

        viewHolder.setImageResource(R.id.setting_icon,item.getIcon());
        viewHolder.setText(R.id.item_title_tv,item.getTitle());
       // viewHolder.titleTv.setText(item.getTitle());
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


//    static class  ViewHolder extends BaseViewHolder{
//
//        @BindView(R.id.item_title_tv)
//        TextView titleTv;
//        @BindView(R.id.item_sub_title_tv)
//        TextView subTitleTv;
//
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this,itemView);
//           // itemView.setOnClickListener(v->adapter.onItemHolderClick(this));
//        }
//    }
}
