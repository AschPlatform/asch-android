package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.view.entity.SettingItem;

/**
 * Created by kimziv on 2017/10/16.
 */

public interface AppSettingContract {

    interface View extends BaseView<Presenter>{
        void displaySettings(List<SettingItem> items);
    }

    interface Presenter extends BasePresenter{
        void loadSettings();
    }
}
