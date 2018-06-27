package asch.so.wallet.contract;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.Block;
import asch.so.wallet.model.entity.Transaction;

/**
 * Created by kimziv on 2017/12/4.
 */

public interface BlockExplorerContract {

    interface View extends BaseView<Presenter>{

        void displayFirstPageBlocks(List<Block> blocks);

        void displayMorePageBlocks(List<Block> blocks);
    }

    interface Presenter extends BasePresenter{
        /**
         * 搜索区块
         * @param blockId
         */
        void searchBlock(String blockId);

        void loadFirstPageBlocks();

        void loadMorePageBlocks();
    }
}
