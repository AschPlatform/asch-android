package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;



public interface RegisterIssuerContract {

    interface View extends BaseView<Presenter>{
        void displaySuccess();

    }

    interface Presenter extends BasePresenter{
        void register(String name,String desc,String secret,String secondSecret);

    }
}
