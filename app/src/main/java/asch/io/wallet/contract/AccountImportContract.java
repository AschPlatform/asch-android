package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.presenter.AccountImportPresenter;

/**
 * Created by kimziv on 2017/9/22.
 */

public interface AccountImportContract {

    interface View extends BaseView<AccountImportPresenter>{
        void displayCheckMessage(String msg);
        void displayImportAccountResult(boolean res, String msg);
    }

    interface Presenter extends BasePresenter{
        void importAccount(String seed, String name);
    }
}
