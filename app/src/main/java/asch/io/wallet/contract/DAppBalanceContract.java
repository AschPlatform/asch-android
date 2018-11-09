package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.DAppBalance;

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
