package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.FullAccount;

/**
 * Created by kimziv on 2017/12/1.
 */

public interface BlockInfoContract {

    interface  View  extends BaseView<Presenter>{
        void displayBlockInfo(FullAccount account);
    }

    interface Presenter extends BasePresenter{
        void loadBlockInfo();
    }
}
