package asch.so.wallet.contract;

import asch.so.base.activity.BaseActivity;
import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/12/11.
 */

public interface AccountInfoContract {

    interface View extends BaseView<Presenter>{
        void displayAccountInfo(Account account);
        void dispLockInfo(String date);
    }

    interface Presenter extends BasePresenter{
        void loadAccountInfo();
    }
}
