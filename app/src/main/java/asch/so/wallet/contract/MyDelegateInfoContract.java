package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Block;

/**
 * Created by kimziv on 2017/12/4.
 */

public interface MyDelegateInfoContract {

    interface View extends BaseView<Presenter>{
        void displayDelegateInfo();

        void displayFirstPageBlocks(List<Block> blocks);

        void displayMorePageBlocks(List<Block> blocks);
    }

    interface Presenter extends BasePresenter{
        void loadDelegateInfo();

        void registerDelegate(String delegateName);

        void loadFirstPageBlocks();

        void loadMorePageBlocks();
    }
}
