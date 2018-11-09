package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.presenter.AssetManagePresenter;

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
