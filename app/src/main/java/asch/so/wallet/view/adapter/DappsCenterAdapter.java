package asch.so.wallet.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import asch.so.wallet.R;
import asch.so.wallet.model.entity.Dapp;

/**
 * Created by kimziv on 2017/10/18.
 */

public class DappsCenterAdapter extends BaseQuickAdapter<Dapp, BaseViewHolder> {

    public DappsCenterAdapter() {
        super(R.layout.item_dapp_center);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, Dapp dapp) {
        viewHolder.setText(R.id.name_tv, dapp.getName())
//                .setText(R.id.lmi_actor, dapp.actors)
//                .setText(R.id.lmi_grade, dapp.grade)
                .setText(R.id.description_tv, dapp.getDescription());
    }

}
