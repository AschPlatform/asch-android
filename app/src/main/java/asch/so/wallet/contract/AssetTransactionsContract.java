package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Transaction;

/**
 * Created by kimziv on 2017/10/13.
 */

public interface AssetTransactionsContract {

    interface View extends BaseView<Presenter>{
        void displayTransactions(List<Transaction> transactions);
    }

    interface Presenter extends BasePresenter{
        void loadTransactions();
    }
}
