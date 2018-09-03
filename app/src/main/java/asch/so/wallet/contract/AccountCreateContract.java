package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;
import rx.Scheduler;

/**
 * Created by kimziv on 2017/9/28.
 */

public interface AccountCreateContract {

    interface  View extends BaseView<Presenter>{

        //void resetSeed(String seed);
        void displayCheckMessage(String msg);
        void displayCreateAccountResult(boolean res, String msg, String secret);
    }

    interface Presenter extends BasePresenter{

        /**
         * 存储账户信息
         * @param name
         */
        void storeAccount(String name);
    }
}
