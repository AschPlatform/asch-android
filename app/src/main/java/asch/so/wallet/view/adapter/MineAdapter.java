package asch.so.wallet.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import asch.so.base.adapter.BaseRecyclerViewAdapter;
import asch.so.wallet.R;
import asch.so.wallet.view.entity.MineItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/28.
 */

public class MineAdapter extends BaseRecyclerViewAdapter <MineAdapter.ViewHolder>{

    private List<MineItem> itemList;

    public MineAdapter(List<MineItem> items) {
        this.itemList=items;
    }

    @Override
    public MineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine,parent, false);



        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MineAdapter.ViewHolder holder, int position) {
        MineItem item=itemList.get(position);
        holder.titleTv.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return itemList==null?0:itemList.size();
    }

    static class  ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_title_tv)
        TextView titleTv;
        @BindView(R.id.item_sub_title_tv)
        TextView subTitleTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
