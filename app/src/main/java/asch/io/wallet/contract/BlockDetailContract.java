package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Block;

/**
 * Created by kimziv on 2018/8/6.
 */

public interface BlockDetailContract {

    interface  View  extends BaseView<BlockDetailContract.Presenter> {
        void displayBlockInfo(Block account);
    }

    interface Presenter extends BasePresenter {
        void loadBlockInfo(String tid);
    }
}
