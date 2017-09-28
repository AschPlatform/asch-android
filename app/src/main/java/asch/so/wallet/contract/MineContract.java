package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.view.entity.MineItem;

/**
 * Created by kimziv on 2017/9/28.
 */

public interface MineContract {

    interface View extends BaseView<Presenter>{
        void displayItems(List<MineItem> items);
    }

    interface Presenter extends BasePresenter{
        void loadItems();
    }
}
