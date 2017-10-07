package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;

/**
 * Created by kimziv on 2017/10/7.
 */

public interface AssetReceiveContract {

    interface View extends BaseView<Presenter> {
        void displayQrCode();
    }

    interface Presenter extends BasePresenter {
        void generateQrCode();
    }
}
