package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.IssuerAssets;

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
