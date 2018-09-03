package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;

/**
 * Created by kimziv on 2017/9/28.
 */

public interface SetWalletPwdContract {

    interface  View extends BaseView<Presenter>{


        void displayCheckMessage(String msg);
        void displayCreateAccountResult(boolean res, String msg, String secret);
    }

    interface Presenter extends BasePresenter{

        /**
         * 存储账户信息
         *
         * @param password
         */
        void createWallet(String password);
    }
}
