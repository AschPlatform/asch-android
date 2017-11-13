package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Transaction;

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
