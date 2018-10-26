package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.IssuerAssets;

/**
 * Created by kimziv on 2017/10/13.
 */

public interface IssuerAssetsContract {

    interface View extends BaseView<Presenter>{
        void displayFirstPageRecords(List<IssuerAssets> records);
        void displayMorePageRecords(List<IssuerAssets> records);
        void showRegisterIssuerDialog();
        void startRegisterAssetsActivity();

    }

    interface Presenter extends BasePresenter{
        void loadFirstPageRecords();
        void loadMorePageRecords();
        void loadIsIssuer();

    }
}
