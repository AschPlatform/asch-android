package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.AschAsset;

/**
 * Created by kimziv on 2017/9/27.
 */

public interface AssetBalanceContract {

    interface View extends BaseView<Presenter>{
        //资产列表
        void displayAssets(List<AschAsset> assetList);
        //XAS余额
        void displayXASBalance(AschAsset balance);
        //用户信息
        void displayAccount(Account account);
    }

    interface Presenter extends BasePresenter{
        void loadAccount();
        void loadAssets();
        void editAssets();
    }
}
