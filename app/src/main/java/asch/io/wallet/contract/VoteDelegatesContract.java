package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Delegate;

/**
 * Created by kimziv on 2017/11/29.
 */

public interface VoteDelegatesContract {

    interface View extends BaseView<Presenter> {
        void displayFirstPageDelegates(List<Delegate> delegates);

        void displayMorePageDelegates(List<Delegate> delegates);

        void displayVoteResult(boolean success, String msg);
    }

    interface Presenter extends BasePresenter {

        void loadFirstPageDelegates();

        void loadMorePageDelegates();

       void voteForDelegates(List<Delegate> delegates,  String secret, String secondSecret);
    }
}
