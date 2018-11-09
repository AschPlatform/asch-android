package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/10/31.
 */

public interface AccountDetailContract {

    interface View extends BaseView<Presenter>{
        void displayAccount(Account account);
    }

    interface Presenter extends BasePresenter{
        void loadAccount(String address);
        Account getAccount();
        void changeAccountName(String name);
    }
}
