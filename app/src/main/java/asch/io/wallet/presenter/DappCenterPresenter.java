package asch.io.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;

import asch.io.wallet.R;
import asch.io.wallet.contract.DappCenterContract;
import asch.io.wallet.model.entity.DApp;

/**
 * Created by kimziv on 2017/10/11.
 */

public class DappCenterPresenter implements DappCenterContract.Presenter{

    private DappCenterContract.View view;
    private Context context;
    public DappCenterPresenter(Context context, DappCenterContract.View view) {
        this.context=context;
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
    public void loadDappList() {

        DApp DApp =new DApp();
        DApp.setName(context.getString(R.string.node_vote));
        DApp.setCategory(1);
        DApp.setDescription(context.getString(R.string.node_vote));

        DApp DApp2 =new DApp();
        DApp2.setName(context.getString(R.string.vote_list));
        DApp2.setCategory(1);
        DApp2.setDescription(context.getString(R.string.vote_list));

        DApp DApp3 =new DApp();
        DApp3.setName(context.getString(R.string.personal_center));
        DApp3.setCategory(2);
        DApp3.setDescription(context.getString(R.string.personal_center));

        DApp DApp4 =new DApp();
        DApp3.setName(context.getString(R.string.cctime));
        DApp3.setCategory(1);
        DApp3.setDescription(context.getString(R.string.cctime));

        ArrayList<DApp> data=new ArrayList<>();
        data.add(DApp);
        data.add(DApp2);
        data.add(DApp3);
        data.add(DApp4);
        this.view.displayDappList(data);

    }
}
