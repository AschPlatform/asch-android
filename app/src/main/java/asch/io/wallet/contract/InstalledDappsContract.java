package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.DApp;

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
