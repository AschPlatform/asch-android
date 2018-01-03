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

//

//        Dapp dapp=new Dapp();
//        dapp.setName("CCTime");
//        dapp.setCategory("news");
//        dapp.setDescription("decentralized news channel.");
//
//        Dapp dapp2=new Dapp();
//        dapp2.setName("孔明屋");
//        dapp2.setCategory("news");
//        dapp2.setDescription("去中心化预测市场");
//
//        Dapp dapp3=new Dapp();
//        dapp3.setName("浏览器");
//        dapp3.setCategory("news");
//        dapp3.setDescription("区块浏览器");
//
//        Dapp dapp4=new Dapp();
//        dapp4.setName("投票");
//        dapp4.setCategory("news");
//        dapp4.setDescription("去中心化投票");

        ArrayList<Dapp> data=new ArrayList<>();
        data.add(dapp);
        data.add(dapp2);
        data.add(dapp3);
        this.view.displayDappList(data);

    }
}
