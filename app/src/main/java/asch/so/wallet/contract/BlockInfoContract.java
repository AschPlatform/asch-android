package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.FullAccount;

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
