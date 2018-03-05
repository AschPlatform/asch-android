package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.DApp;

/**
 * Created by kimziv on 2018/1/24.
 */

public interface InstalledDappsContract {

    interface View extends BaseView<InstalledDappsContract.Presenter> {
        void displayInstalledDapps(List<DApp> dapps);
    }

    interface Presenter extends BasePresenter {
        void loadInstalledDapps();
    }
}
