package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
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

import static asch.so.wallet.AppConstants.DEFAULT_PAGE_SIZE;

/**
 * Created by kimziv on 2017/12/4.
 */

public class BlockExplorerPresenter implements BlockExplorerContract.Presenter {

    private BlockExplorerContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;
    private IPage pager;
    private boolean serchBolck = false;
    private int listLastBlockHeight = 0;

    public BlockExplorerPresenter(Context context, BlockExplorerContract.View view) {
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
                loadBlocks(param1, param2,null);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(DEFAULT_PAGE_SIZE);
    }

    private void loadBlocks(int pageIndex, int pageSize,String idOrHeight) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<Block>>) subscriber -> {
            AschResult result;
            BlockQueryParameters params = new BlockQueryParameters();
            if(!serchBolck){
                params.orderByDescending("height")
                        .setOffset(offset)
                        .setLimit(limit);
                params.setHeight(-1);
                result = AschSDK.Block.queryBlocks(params);
            }else{
                if(isBlockHeight(idOrHeight)){
                    result = AschSDK.Block.getBlockByHeight(Integer.valueOf(idOrHeight),true);
                }else{
                    result = AschSDK.Block.getBlockById(idOrHeight,true);
                }
            }

            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                List<Block> blocks = new ArrayList<>();
                if(!serchBolck){
                    JSONArray BlocksJsonArray = resultJSONObj.getJSONArray("blocks");
                    blocks.addAll(JSON.parseArray(BlocksJsonArray.toJSONString(), Block.class));
                }else{
                    JSONObject blockObj = resultJSONObj.getJSONObject("block");
                    blocks.add(JSON.parseObject(blockObj.toJSONString(),Block.class));
                }
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
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<Block> blocks) {
                        if(serchBolck){
                            view.displayFirstPageBlocks(blocks);
                        }else{
                            filterBlocks(blocks);
                            listLastBlockHeight = blocks.size()>0?(blocks.get(blocks.size()-1).getHeight()):0;
                            if (pager.isFirstPage()) {
                                view.displayFirstPageBlocks(blocks);
                            } else {
                                view.displayMorePageBlocks(blocks);
                            }
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
        serchBolck = true;
        loadBlocks(0,1,blockId);
    }

    @Override
    public void loadFirstPageBlocks() {
        serchBolck = false;
        pager.loadPage(true);
    }

    @Override
    public void loadMorePageBlocks() {
        serchBolck = false;
        pager.loadPage(false);
    }

    private void filterBlocks(List<Block> blocks){
        if(listLastBlockHeight==0){
            return ;
        }
        List<Block> list = new ArrayList<>();
        for (Block block:blocks){
            if(block.getHeight()>=listLastBlockHeight){
                list.add(block);
            }else{
                blocks.removeAll(list);
                return ;
            }
        }
    }


    public static boolean isBlockHeight(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        boolean isNum = pattern.matcher(str).matches();
        if(isNum && str.length()<11){
            return true;
        }else{
            return false;
        }
    }

}
