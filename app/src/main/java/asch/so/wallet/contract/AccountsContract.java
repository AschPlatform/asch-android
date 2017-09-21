package asch.so.wallet.contract;

import java.io.InvalidClassException;
import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountsPresenter;

/**
 * Created by kimziv on 2017/9/21.
 */

public interface AccountsContract {

    interface View extends BaseView<AccountsPresenter> {
        void displaySavedAccounts(List<Account> accountList);
    }

    interface Presenter extends BasePresenter{
        void loadSavedAccounts();

        void saveCurrentAccountToPreference(String address) throws InvalidClassException;
    }
}
