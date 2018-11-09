package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/12/11.
 */

public interface AccountInfoContract {

    interface View extends BaseView<Presenter>{
        void displayAccountInfo(Account account);
        void dispLockInfo(String lockeAmount, String lockedDate);
    }

    interface Presenter extends BasePresenter{
        void loadAccountInfo();
    }
}
