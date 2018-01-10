package asch.so.base.adapter.page;

/**
 * Created by kimziv on 2017/11/17.
 */

public abstract class Page1 extends IPage {

    @Override
    public int handlePageIndex(int currPageIndex, int pageSize) {
        return ++currPageIndex;
    }

    @Override
    protected int handlePage(int currPageIndex, int pageSize) {
        return pageSize;
    }
}
