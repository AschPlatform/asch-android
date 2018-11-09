package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;



public interface RegisterIssuerContract {

    interface View extends BaseView<Presenter>{
        void displaySuccess();

    }

    interface Presenter extends BasePresenter{
        void register(String name,String desc,String secret,String secondSecret);

    }
}
