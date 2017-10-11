package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;

/**
 * Created by kimziv on 2017/10/11.
 */

public interface DappContainerContract {

    interface View extends BaseView<Presenter>{
        void displayDapp();
    }

    interface Presenter extends BasePresenter{
        void loadDapp();
    }
}
