package asch.io.wallet.contract;

import java.util.LinkedHashMap;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.BaseAsset;

/**
 * Created by kimziv on 2017/9/29.
 */

public interface DAppDepositContract {

    interface View extends BaseView<Presenter>{
        void displayAssets(LinkedHashMap<String, BaseAsset> assetsMap);
        void displayTransferResult(boolean res, String msg);
        void displayPasswordValidMessage(boolean res, String msg);
    }

    interface Presenter extends BasePresenter{

        default void transfer(String dappId, String currency, long amount, String message, String secret, String secondSecret, String password) {

        }

        void loadAssets(String currency, boolean ignoreCache);
    }
}
