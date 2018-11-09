package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Transaction;

/**
 * Created by kimziv on 2017/10/18.
 */

public interface TransactionsContract {
    interface View extends BaseView<Presenter> {
        void displayFirstPageTransactions(List<Transaction> transactions);

        void displayMorePageTransactions(List<Transaction> transactions);
    }

    interface Presenter extends BasePresenter {

        void loadFirstPageTransactions();

        void loadMorePageTransactions();
    }
}
