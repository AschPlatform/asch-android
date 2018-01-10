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
        list.add(new MineSection(new MineItem(R.mipmap.my_account_managment,context.getString(R.string.account_management))) );
        list.add(new MineSection(new MineItem(R.mipmap.my_bill,context.getString(R.string.bill))) );
        list.add(new MineSection(new MineItem(R.mipmap.my_block_info,context.getString(R.string.block_details))));

        list.add(new MineSection(true,"Section 2"));
        list.add(new MineSection(new MineItem(R.mipmap.my_settings,context.getString(R.string.set))));

        list.add(new MineSection(new MineItem(R.mipmap.my_user,context.getString(R.string.use_explain))));
        list.add(new MineSection(new MineItem(R.mipmap.my_users,context.getString(R.string.about_us))));


        view.displayItems(list);
    }




    @Override
    public void update(java.util.Observable observable, Object obj) {
        if (observable instanceof AccountsManager){
            loadAccount();
        }
    }
}
