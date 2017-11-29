package asch.so.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;

import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.MineContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.view.entity.MineItem;
import asch.so.wallet.view.entity.MineSection;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by kimziv on 2017/9/28.
 */

public class MinePresenter implements MineContract.Presenter, java.util.Observer {

    private MineContract.View view;
    private Context context;

    public MinePresenter(Context context, MineContract.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
        AccountsManager.getInstance().addObserver(this);
    }

    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void loadAccount() {
        view.displayAccount(getAccount());
    }

    @Override
    public void loadItems() {
        ArrayList<MineSection> list=new ArrayList<>();

        list.add(new MineSection(true,"Section 1"));
        list.add(new MineSection(new MineItem(R.mipmap.my_account_managment,"账户管理")) );
        list.add(new MineSection(new MineItem(R.mipmap.my_settings,"设置")));
        list.add(new MineSection(true,"Section 2"));
        list.add(new MineSection(new MineItem(R.mipmap.my_user,"节点列表")));
        list.add(new MineSection(new MineItem(R.mipmap.my_bell,"投票")));
       // list.add(new MineSection(new MineItem(R.mipmap.my_block_info,"交易记录")));
        list.add(new MineSection(true,"Section 3"));
        list.add(new MineSection(new MineItem(R.mipmap.my_block_info,"区块详情")));
        list.add(new MineSection(new MineItem(R.mipmap.my_users,"关于我们")));


        view.displayItems(list);
    }




    @Override
    public void update(java.util.Observable observable, Object obj) {
        if (observable instanceof AccountsManager){
            loadAccount();
        }
    }
}
