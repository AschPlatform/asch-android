package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;

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
