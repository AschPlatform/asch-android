package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;


public interface RegisterAssetContract {

    interface View extends BaseView<Presenter>{
        void displaySuccess();

    }

    interface Presenter extends BasePresenter{
        void register(String currency, String desc, String maximum, String precision, String secret, String secondSecret);

    }
}
