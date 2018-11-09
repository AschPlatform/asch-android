package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;

/**
 * Created by haizeiwang on 2018/1/22.
 */

public interface SecondSecretContract {

    interface  View extends BaseView<Presenter> {

        void displaySetSecondSecretResult(boolean res, String msg);

    }

    interface Presenter extends BasePresenter {

        /**
         * 保存二级密码
         * @param password asch账户密码
         * @param secondSecret  asch账户二级密码，最小长度：1，最大长度：100
         */
        void storeSecondPassword(String secondSecret,String password);

    }
}
