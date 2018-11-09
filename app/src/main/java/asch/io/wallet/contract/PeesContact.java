package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.PeerNode;

/**
 * Created by kimziv on 2017/11/24.
 */

public interface PeesContact {

    interface View extends BaseView<Presenter>{
        void displayFirstPagePeers(List<PeerNode> peers);

        void displayMorePagePeers(List<PeerNode> peers);
    }

    interface Presenter extends BasePresenter{
        void loadFirstPagePeers();

        void loadMorePagePeers();
    }
}
