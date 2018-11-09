package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.DApp;

/**
 * Created by kimziv on 2018/2/5.
 */

public interface DAppDetailContract {

    interface  View extends BaseView<Presenter>{
        void displayDApp(DApp taskModel);
    }

    interface Presenter extends BasePresenter{
        void  loadDApp(String dappId);
    }
}
