package asch.so.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import asch.so.wallet.R;
import asch.so.wallet.contract.DappCenterContract;
import asch.so.wallet.model.entity.Dapp;

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

        Dapp dapp=new Dapp();
        dapp.setName(context.getString(R.string.node_vote));
        dapp.setCategory("system");
        dapp.setDescription(context.getString(R.string.node_vote));

        Dapp dapp2=new Dapp();
        dapp2.setName(context.getString(R.string.vote_list));
        dapp2.setCategory("system");
        dapp2.setDescription(context.getString(R.string.vote_list));

        Dapp dapp3=new Dapp();
        dapp3.setName(context.getString(R.string.personal_center));
        dapp3.setCategory("system");
        dapp3.setDescription(context.getString(R.string.personal_center));

        Dapp dapp4=new Dapp();
        dapp3.setName(context.getString(R.string.cctime));
        dapp3.setCategory("cctime");
        dapp3.setDescription(context.getString(R.string.cctime));

        ArrayList<Dapp> data=new ArrayList<>();
        data.add(dapp);
        data.add(dapp2);
        data.add(dapp3);
        data.add(dapp4);
        this.view.displayDappList(data);

    }
}
