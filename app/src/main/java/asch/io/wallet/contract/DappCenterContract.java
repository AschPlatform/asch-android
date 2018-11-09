package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.DApp;

/**
 * Created by kimziv on 2017/10/11.
 */

public interface DappCenterContract {
    interface View extends BaseView<Presenter>{
        void displayDappList(List<DApp> DApps);
    }

    interface Presenter extends BasePresenter{
        void loadDappList();
    }
}
