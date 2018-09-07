package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;

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
         * @param secret asch账户密码
         * @param secondSecret  asch账户二级密码，最小长度：1，最大长度：100
         */
        void storeSecondPassword(String secondSecret);
    }
}
