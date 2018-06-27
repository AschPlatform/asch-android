package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.DAppBalance;
import asch.so.wallet.model.entity.PeerNode;

/**
 * Created by kimziv on 2018/2/9.
 */

public interface DAppBalanceContract {

    interface View extends BaseView<Presenter>{
        void displayFirstPageBalances(List<DAppBalance> peers);

        void displayMorePageBalances(List<DAppBalance> peers);
    }

    interface Presenter extends BasePresenter{
        void loadFirstPageBalances();

        void loadMorePageBalances();
    }
}
