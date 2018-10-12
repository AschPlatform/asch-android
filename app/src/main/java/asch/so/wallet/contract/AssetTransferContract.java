package asch.so.wallet.contract;

import java.util.LinkedHashMap;
import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.AschAsset;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.UIAAsset;

/**
 * Created by kimziv on 2017/9/29.
 */

public interface AssetTransferContract {

    interface View extends BaseView<Presenter>{
       // void displayToast(String toast);
        //void displayAssets(List<UIAAsset> assets, int selectIndex);
        void displayAssets(LinkedHashMap<String,AschAsset> assetsMap);
        void displayTransferResult(boolean res, String msg);
        void displayPasswordValidMessage(boolean res, String msg);
    }

    interface Presenter extends BasePresenter{

        void transfer(String currency, String targetAddress, long amount, String message, String secret, String secondSecret,String password);

        void loadAssets(String currency, boolean ignoreCache);
    }
}
