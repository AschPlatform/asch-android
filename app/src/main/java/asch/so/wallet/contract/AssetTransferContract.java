package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.UIAAsset;

/**
 * Created by kimziv on 2017/9/29.
 */

public interface AssetTransferContract {

    interface View extends BaseView<Presenter>{
        void displayToast(String toast);
        void displayAssets(List<UIAAsset> assets, int selectIndex);
    }

    interface Presenter extends BasePresenter{

        void transfer(String currency, String targetAddress, long amount, String message, String secret, String secondSecret);

        void loadAssets(String currency);
    }
}
