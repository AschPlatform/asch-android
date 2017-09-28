package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;

/**
 * Created by kimziv on 2017/9/27.
 */

public interface AssetsContract {

    interface View extends BaseView<Presenter>{
        void displayAssets(List<Balance> assetList);
    }

    interface Presenter extends BasePresenter{
        void loadAssets();
    }
}
