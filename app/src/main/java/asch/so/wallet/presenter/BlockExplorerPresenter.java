package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.BlockExplorerContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Block;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.TransferAsset;
import asch.so.wallet.model.entity.UIATransferAsset;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.dto.query.BlockQueryParameters;

/**
 * Created by kimziv on 2017/12/4.
 */

public class BlockExplorerPresenter implements BlockExplorerContract.Presenter {

    private BlockExplorerContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;
    private IPage pager;

    public BlockExplorerPresenter(BlockExplorerContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.view.setPresenter(this);
        subscriptions=new CompositeSubscription();
        initPager();
    }

    private void initPager() {
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                loadBlocks(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(AppConstants.DEFAULT_PAGE_SIZE);
    }

    private void loadBlocks(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        String address = getAccount().getAddress();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Block>>) subscriber -> {
            BlockQueryParameters params = new BlockQueryParameters()
                    .orderByDescending("height")
                    .setOffset(offset)
                    .setLimit(limit);

            AschResult result = AschSDK.Block.queryBlocks(params);
            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray BlocksJsonArray = resultJSONObj.getJSONArray("blocks");
                List<Block> blocks = JSON.parseArray(BlocksJsonArray.toJSONString(), Block.class);
                subscriber.onNext(blocks);
                subscriber.onCompleted();
            } else {
                subscriber.onError(new Throwable(result.getError()));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Block>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable("网络错误"));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<Block> blocks) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPageBlocks(blocks);
                        } else {
                            view.displayMorePageBlocks(blocks);
                        }
                        pager.finishLoad(true);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void searchBlock(String blockId) {

    }

    @Override
    public void loadFirstPageBlocks() {
        loadFirstPageBlocks();
    }

    @Override
    public void loadMorePageBlocks() {
        loadMorePageBlocks();
    }

    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
