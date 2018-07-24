package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.FullAccount;

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
