package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Transaction;

/**
 * Created by kimziv on 2017/11/13.
 */

public interface TransactionDetailContract {

    interface View extends BaseView<BasePresenter> {
        void displayTranscations(Transaction transaction);
    }

    interface Presenter extends BasePresenter {
        void loadTransaction();
    }
}
