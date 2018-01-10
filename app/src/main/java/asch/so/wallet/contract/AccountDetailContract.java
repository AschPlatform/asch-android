package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;

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
