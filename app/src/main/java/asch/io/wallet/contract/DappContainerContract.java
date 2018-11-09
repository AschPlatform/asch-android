package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;

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
