package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.AschAsset;
import asch.so.wallet.model.entity.Balance;

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
