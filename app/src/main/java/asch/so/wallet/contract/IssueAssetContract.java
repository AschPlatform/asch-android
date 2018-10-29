package asch.so.wallet.contract;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;



public interface IssueAssetContract {

    interface View extends BaseView<Presenter>{
        void displaySuccess();
    }

    interface Presenter extends BasePresenter{
        void issueAsset(String name,String amount,String password,String secondPassword);
    }
}
