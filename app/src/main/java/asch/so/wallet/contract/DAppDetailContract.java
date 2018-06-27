package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.DApp;

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
