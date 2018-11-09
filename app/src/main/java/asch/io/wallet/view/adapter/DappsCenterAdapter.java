package asch.io.wallet.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.io.wallet.R;
import asch.io.wallet.model.entity.DApp;

/**
 * Created by kimziv on 2017/10/18.
 */

public class DappsCenterAdapter extends BaseQuickAdapter<DApp, BaseViewHolder> {

    public DappsCenterAdapter() {
        super(R.layout.item_dapp_center);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, DApp DApp) {
        viewHolder.setText(R.id.name_tv, DApp.getName())
//                .setText(R.id.lmi_actor, dapp.actors)
//                .setText(R.id.lmi_grade, dapp.grade)
                .setText(R.id.description_tv, DApp.getDescription());
    }

}
