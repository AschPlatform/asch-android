package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;


public interface AssetWithdrawContract {

    interface View extends BaseView<Presenter>{

        void displayTransferResult(boolean res, String msg);
        void displayPasswordValidMessage(boolean res, String msg);
    }

    interface Presenter extends BasePresenter{

        void withdraw(String currency, String gateway,String fee,String targetAddress, Long amount,
                      String message, String secret, String secondSecret, String password);

    }
}
