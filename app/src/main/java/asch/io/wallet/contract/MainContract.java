package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/11/15.
 */

public interface MainContract {

    interface View extends BaseView<Presenter>{
        void displayAccount(Account account);
    }

    interface Presenter extends BasePresenter{
        void loadFullAccount();
    }
}
