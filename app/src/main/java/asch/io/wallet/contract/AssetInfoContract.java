package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.UIAAsset;

/**
 * Created by kimziv on 2017/11/5.
 */

public interface AssetInfoContract {

    interface View extends BaseView<Presenter> {
        //资产列表
        void displayAssets(List<UIAAsset> assetList);
    }

    interface Presenter extends BasePresenter {
        void loadAssets();
    }
}
