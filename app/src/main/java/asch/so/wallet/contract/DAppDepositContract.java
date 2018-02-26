package asch.so.wallet.contract;

import java.util.LinkedHashMap;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.BaseAsset;

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

        void transfer(String currency, String dappId, long amount, String message, String secret, String secondSecret, String password);

        void loadAssets(String currency, boolean ignoreCache);
    }
}
