package asch.so.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;

import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.DiscoveryContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.view.entity.MineItem;
import asch.so.wallet.view.entity.MineSection;

/**
 * Created by kimziv on 2017/9/28.
 */

public class DiscoveryPresenter implements DiscoveryContract.Presenter {

    private DiscoveryContract.View view;
    private Context context;

    public DiscoveryPresenter(Context context, DiscoveryContract.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
        //AccountsManager.getInstance().addObserver(this);
    }

    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

//    @Override
//    public void loadAccount() {
//        view.displayAccount(getAccount());
//    }

    @Override
    public void loadItems() {
        ArrayList<MineSection> list=new ArrayList<>();

        list.add(new MineSection(true,"Section 1"));
        list.add(new MineSection(new MineItem(R.mipmap.node_vote,"发行资产")));
        list.add(new MineSection(new MineItem(R.mipmap.my_contacts,context.getString(R.string.block_browse))));
        view.displayItems(list);
    }




//    @Override
//    public void update(java.util.Observable observable, Object obj) {
//        if (observable instanceof AccountsManager){
//            loadAccount();
//        }
//    }
}
