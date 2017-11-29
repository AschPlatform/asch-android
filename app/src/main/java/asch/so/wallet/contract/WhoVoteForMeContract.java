package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.model.entity.Voter;

/**
 * Created by kimziv on 2017/11/29.
 */

public interface WhoVoteForMeContract {

    interface View extends BaseView<Presenter> {
        void displayFirstPageVoters(List<Voter> voters);

        void displayMorePageVoters(List<Voter> voters);
    }

    interface Presenter extends BasePresenter {

        void loadFirstPageVoters();

        void loadMorePageVoters();
    }
}
