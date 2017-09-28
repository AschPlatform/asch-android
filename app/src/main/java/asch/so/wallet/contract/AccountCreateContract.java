package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Account;

/**
 * Created by kimziv on 2017/9/28.
 */

public interface AccountCreateContract {

    interface  View extends BaseView<Presenter>{

        void resetSeed(String seed);

    }

    interface Presenter extends BasePresenter{

        /**
         * 存储账户信息
         * @param seed
         * @param name
         * @param password
         * @param hint
         */
        void storeAccount(String seed, String name, String password, String hint);

        void generateSeed();


    }
}
