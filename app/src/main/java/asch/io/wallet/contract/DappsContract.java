package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.DApp;

/**
 * Created by kimziv on 2018/1/10.
 */

public interface DappsContract {
    interface View extends BaseView<Presenter>{
        void displayFirstPageDapps(List<DApp> DApps);

        void displayMorePageDapps(List<DApp> DApps);
    }

    interface Presenter extends BasePresenter{
        void loadFirstPageDapps();

        void loadMorePageDapps();
    }
}
