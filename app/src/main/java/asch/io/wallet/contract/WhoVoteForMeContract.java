package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Voter;

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
