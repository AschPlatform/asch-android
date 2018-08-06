package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Block;
import asch.so.wallet.model.entity.FullAccount;

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
