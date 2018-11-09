package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;


public interface RegisterAssetContract {

    interface View extends BaseView<Presenter>{
        void displaySuccess();

    }

    interface Presenter extends BasePresenter{
        void register(String currency, String desc, String maximum, String precision, String secret, String secondSecret);

    }
}
