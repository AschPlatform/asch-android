package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;

/**
 * Created by kimziv on 2017/12/13.
 */

public interface LockCoinsContract {

    interface View extends BaseView<Presenter>{
        void displayLockCoinsResult(boolean success, String msg);
    }

    interface Presenter extends BasePresenter{
        void lockCoins(long height, String secret, String secondSecret);
    }
}
