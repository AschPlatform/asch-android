package asch.so.wallet.contract;

import java.io.InvalidClassException;
import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.AschAsset;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.presenter.AssetManagePresenter;

/**
 * Created by Deng on 2018/9/27.
 */

public interface AssetManageContract {

    interface View extends BaseView<AssetManagePresenter> {
        void displayUiaAssets(List<AschAsset> assets);
        void displayGatewayAssets(List<AschAsset> assets);
    }

    interface Presenter extends BasePresenter{

        void saveCurrentAssets(String address);
        void loadAllAssets();
    }
}
