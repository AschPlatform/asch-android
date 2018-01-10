package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Dapp;

/**
 * Created by kimziv on 2017/10/11.
 */

public interface DappCenterContract {
    interface View extends BaseView<Presenter>{
        void displayDappList(List<Dapp> dapps);
    }

    interface Presenter extends BasePresenter{
        void loadDappList();
    }
}
