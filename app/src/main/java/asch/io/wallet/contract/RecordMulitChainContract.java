package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;

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
