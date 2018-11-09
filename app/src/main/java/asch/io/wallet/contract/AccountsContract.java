package asch.io.wallet.contract;

import java.io.InvalidClassException;
import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.presenter.AccountsPresenter;

/**
 * Created by kimziv on 2017/9/21.
 */

public interface AccountsContract {

    interface View extends BaseView<AccountsPresenter> {
        void displaySavedAccounts(List<Account> accountList);
        void gotoCreateAccount();
        void gotoImportAccount();
    }

    interface Presenter extends BasePresenter{
        void loadSavedAccounts();

        void setCurrentAccount(Account account);

        void saveCurrentAccountToPreference(String address) throws InvalidClassException;
    }
}
