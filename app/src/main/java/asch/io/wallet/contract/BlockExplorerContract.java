package asch.io.wallet.contract;

import java.util.List;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.Block;

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
