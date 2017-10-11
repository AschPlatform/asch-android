package asch.so.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
        dapp.setName("CCTime");
        dapp.setCategory("news");
        dapp.setDescription("decentralized news channel.");

        ArrayList<Dapp> data=new ArrayList<>();
        data.add(dapp);
        this.view.displayDappList(data);

    }
}
