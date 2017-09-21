package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountsPresenter;

/**
 * Created by kimziv on 2017/9/21.
 */

public interface AccountsContract {

    interface View extends BaseView<AccountsPresenter> {

    }

    interface Presenter extends BasePresenter{

    }
}
