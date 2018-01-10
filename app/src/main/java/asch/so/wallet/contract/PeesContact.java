package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.PeerNode;
import asch.so.wallet.model.entity.Transaction;

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
