package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/12/13.
 */

public interface LockCoinsContract {

    interface View extends BaseView<Presenter>{
        void displayLockCoinsResult(boolean success, String msg);
        void displayBlockInfo(Account account);
        void displayLockFee(String fee);
    }

    interface Presenter extends BasePresenter{
        void lockCoins(long amount, long height, String secret, String secondSecret);
        void loadBlockInfo();
        void  queryLockFee();
    }
}
