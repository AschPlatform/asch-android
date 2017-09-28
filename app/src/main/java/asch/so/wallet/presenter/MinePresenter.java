package asch.so.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;

import asch.so.wallet.contract.MineContract;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.view.entity.MineItem;

/**
 * Created by kimziv on 2017/9/28.
 */

public class MinePresenter implements MineContract.Presenter {

    private MineContract.View view;
    private Context context;

    public MinePresenter(Context context, MineContract.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
    }

    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void loadItems() {
        ArrayList<MineItem> list=new ArrayList<>();
        list.add(new MineItem());
        list.add(new MineItem());
        list.add(new MineItem());
        list.add(new MineItem());
        list.add(new MineItem());

        view.displayItems(list);
    }
}
