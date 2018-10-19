package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.AschAsset;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Transaction;

/**
 * Created by kimziv on 2017/10/13.
 */

public interface AssetTransactionsContract {

    interface View extends BaseView<Presenter>{
        void displayBalance(AschAsset balance);
        void showDeposit(String address);
        void showCreateAccountDialog();
    }

    interface Presenter extends BasePresenter{
        void loadGatewayAddress(String gateway,String address);
        void createGatewayAccount(String gateway, String message, String secret, String secondSecret);
    }
}
