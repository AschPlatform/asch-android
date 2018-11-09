package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.AschAsset;

/**
 * Created by kimziv on 2017/10/13.
 */

public interface AssetTransactionsContract {

    interface View extends BaseView<Presenter>{
        void displayBalance(AschAsset balance);
        void showDeposit(String address);
        void showCreateAccountDialog();
        void showCreateSuccessDialog();
    }

    interface Presenter extends BasePresenter{
        void loadGatewayAddress(String gateway,String address);
        void createGatewayAccount(String gateway, String message, String secret, String secondSecret);
    }
}
