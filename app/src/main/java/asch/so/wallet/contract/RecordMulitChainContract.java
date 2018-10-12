package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Deposit;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.Withdraw;

/**
 * Created by kimziv on 2017/10/13.
 */

public interface RecordMulitChainContract {

    interface View extends BaseView<Presenter>{
        void displayFirstPageRecords(List<?> records);
        void displayMorePageRecords(List<?> records);

    }

    interface Presenter extends BasePresenter{
        void loadFirstPageRecords();
        void loadMorePageRecords();

    }
}
