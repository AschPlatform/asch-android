package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;

/**
 * Created by kimziv on 2017/9/29.
 */

public interface AssetTransferContract {

    interface View extends BaseView<Presenter>{
       // void displayToast(String toast);
        //void displayAssets(List<UIAAsset> assets, int selectIndex);
        void displayTransferResult(boolean res, String msg);
        void displayPasswordValidMessage(boolean res, String msg);
    }

    interface Presenter extends BasePresenter{

        void transfer(String currency, String targetAddress, String amount, String message, String secret, String secondSecret,String password);

    }
}
