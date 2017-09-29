package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;

/**
 * Created by kimziv on 2017/9/29.
 */

public interface AssetTransferContract {

    interface View extends BaseView<Presenter>{
        void displayToast(String toast);
    }

    interface Presenter extends BasePresenter{

        void transfer(String currency, String targetAddress, long amount, String message, String secret, String secondSecret);

    }
}
