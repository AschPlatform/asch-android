package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.model.entity.PeerNode;

/**
 * Created by kimziv on 2018/1/10.
 */

public interface DappsContract {
    interface View extends BaseView<Presenter>{
        void displayFirstPageDapps(List<Dapp> dapps);

        void displayMorePageDapps(List<Dapp> dapps);
    }

    interface Presenter extends BasePresenter{
        void loadFirstPageDapps();

        void loadMorePageDapps();
    }
}
