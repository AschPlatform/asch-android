package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.BlockDetailContract;
import asch.so.wallet.contract.BlockInfoContract;
import asch.so.wallet.model.entity.Block;
import asch.so.wallet.model.entity.FullAccount;
import asch.so.wallet.util.AppUtil;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2018/8/6.
 */

public class BlockDetailPresenter implements BlockDetailContract.Presenter {
    private Context context;
    private BlockDetailContract.View view;
    private CompositeSubscription subscriptions;


    public BlockDetailPresenter(Context context, BlockDetailContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        subscriptions=new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadBlockInfo(String tid) {
        Subscription subscription = Observable.create((Observable.OnSubscribe<AschResult>) subscriber -> {
            AschResult result = AschSDK.Block.getBlockById(tid);
            if (result != null && result.isSuccessful()) {
                subscriber.onNext(result);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(e);
                    }

                    @Override
                    public void onNext(AschResult result) {

                        if (result.isSuccessful())
                        {
                            JSONObject resultJson = JSON.parseObject(result.getRawJson());
                            JSONObject  blockJson =resultJson.getJSONObject("block");
                            Block block=JSON.parseObject(blockJson.toJSONString(),Block.class);
                            view.displayBlockInfo(block);
                        }else {
                            view.displayBlockInfo(null);
                        }
                    }
                });
        subscriptions.add(subscription);
    }
}
