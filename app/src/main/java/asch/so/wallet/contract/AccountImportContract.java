package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.presenter.AccountImportPresenter;

/**
 * Created by kimziv on 2017/9/22.
 */

public interface AccountImportContract {

    interface View extends BaseView<AccountImportPresenter>{
        void displayCheckMessage(String msg);
        void displayImportAccountResult(boolean res, String msg);
    }

    interface Presenter extends BasePresenter{
        void importAccount(String seed, String name, String password, String hint);
    }
}
