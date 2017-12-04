package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Delegate;

/**
 * Created by kimziv on 2017/11/29.
 */

public interface MyVoteRecordContract {
    interface View extends BaseView<Presenter> {
        void displayFirstPageDelegates(List<Delegate> delegates);

        void displayMorePageDelegates(List<Delegate> delegates);
    }

    interface Presenter extends BasePresenter {

        void loadFirstPageDelegates();

        void loadMorePageDelegates();
    }
}