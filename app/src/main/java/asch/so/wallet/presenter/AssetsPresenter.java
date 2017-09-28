package asch.so.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.wallet.contract.AssetsContract;
import asch.so.wallet.model.entity.BaseAsset;

/**
 * Created by kimziv on 2017/9/20.
 */

public class AssetsPresenter implements AssetsContract.Presenter {

    private final  AssetsContract.View view;
    private Context context;

    public AssetsPresenter(AssetsContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void loadAssets() {
        ArrayList<BaseAsset> list=new ArrayList<>();
        list.add(new BaseAsset());
        list.add(new BaseAsset());
        list.add(new BaseAsset());
        list.add(new BaseAsset());
        list.add(new BaseAsset());

        view.displayAssets(list);
    }
}
