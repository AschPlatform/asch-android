package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.view.entity.MineSection;

/**
 * Created by kimziv on 2017/9/28.
 */

public interface DiscoveryContract {

    interface View extends BaseView<Presenter>{
        //void displayAccount(Account account);
        void displayItems(List<MineSection> items);
    }

    interface Presenter extends BasePresenter{
       // void loadAccount();
        void loadItems();
    }
}
