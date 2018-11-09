package asch.io.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import asch.io.wallet.AppConstants;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.AccountInfoContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.util.AppUtil;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/12/11.
 */

public class AccountInfoPresenter implements AccountInfoContract.Presenter {

    private Context context;
    private AccountInfoContract.View view;
    private CompositeSubscription subscriptions;

    public AccountInfoPresenter(Context context, AccountInfoContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        this.subscriptions=new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadAccountInfo() {
        this.view.displayAccountInfo(getAccount());
        if(getAccount().hasLockCoins()){
            getLastHeight();
        }
    }

    public void getLastHeight() {
        Subscription subscription = Observable.create((Observable.OnSubscribe<AschResult>) subscriber -> {
            AschResult result = AschSDK.Block.getHeight();
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
                        JSONObject obj = JSON.parseObject(result.getRawJson());
                        if(obj.getBoolean("success")){
                            int lastHeight = obj.getIntValue("height");
                            getAccount().getFullAccount().getLatestBlock().setHeight(lastHeight);
                            long lockHeight = getAccount().getFullAccount().getAccount().getLockHeight();
                            Calendar calendar = AppUtil.getDateByHeight(lockHeight-lastHeight);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            long lockedAmount=getAccount().getFullAccount().getAccount().getLockedAmount();
                            String lockedAmountText=AppUtil.decimalFormat(AppUtil.decimalFromBigint(lockedAmount, AppConstants.PRECISION));
                            view.dispLockInfo(lockedAmountText, sdf.format(calendar.getTime()));
                        }
                    }
                });
        subscriptions.add(subscription);
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
