package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.view.entity.SettingItem;

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
