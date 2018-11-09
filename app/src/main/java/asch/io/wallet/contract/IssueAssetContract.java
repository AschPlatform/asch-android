package asch.io.wallet.contract;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;



public interface IssueAssetContract {

    interface View extends BaseView<Presenter>{
        void displaySuccess();
    }

    interface Presenter extends BasePresenter{
        void issueAsset(String name,String amount,String password,String secondPassword);
    }
}
