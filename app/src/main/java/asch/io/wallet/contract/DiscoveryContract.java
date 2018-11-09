package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.view.entity.MineSection;

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
