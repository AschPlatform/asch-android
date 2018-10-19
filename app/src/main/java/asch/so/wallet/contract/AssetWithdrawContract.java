package asch.so.wallet.contract;

import java.util.LinkedHashMap;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.AschAsset;



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
