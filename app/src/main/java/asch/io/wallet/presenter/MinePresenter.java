package asch.io.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;

import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.MineContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.view.entity.MineItem;
import asch.io.wallet.view.entity.MineSection;

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
        list.add(new MineSection(new MineItem(R.mipmap.icon_anto,context.getString(R.string.account_management))) );
        list.add(new MineSection(new MineItem(R.mipmap.icon_safe,context.getString(R.string.secure_setting))));
//        list.add(new MineSection(new MineItem(R.mipmap.my_bill,context.getString(R.string.bill))) );

//        list.add(new MineSection(true,"Section 2"));
//        list.add(new MineSection(new MineItem(R.mipmap.personal_center,context.getString(R.string.personal_center))));
//        //list.add(new MineSection(new MineItem(R.mipmap.node_vote,context.getString(R.string.node_vote))));
//       // list.add(new MineSection(new MineItem(R.mipmap.vote_list,context.getString(R.string.vote_list))));
//        list.add(new MineSection(new MineItem(R.mipmap.my_block_info,context.getString(R.string.block_details))));
//        //list.add(new MineSection(new MineItem(R.mipmap.my_contacts,context.getString(R.string.block_browse))));

        list.add(new MineSection(true,"Section 2"));
        list.add(new MineSection(new MineItem(R.mipmap.icon_shez,context.getString(R.string.set))));
        list.add(new MineSection(new MineItem(R.mipmap.icon_shiy,context.getString(R.string.use_explain))));
        list.add(new MineSection(new MineItem(R.mipmap.icon_myuser,context.getString(R.string.about_us))));

        view.displayItems(list);
    }




    @Override
    public void update(java.util.Observable observable, Object obj) {
        if (observable instanceof AccountsManager){
            loadAccount();
        }
    }
}
