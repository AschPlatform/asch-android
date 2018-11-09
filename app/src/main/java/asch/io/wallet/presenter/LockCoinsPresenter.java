package asch.io.wallet.presenter;

import android.content.Context;

import asch.io.base.view.Throwable;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.LockCoinsContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.fragment.LockCoinsFragment;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.FeeCalculater;

/**
 * Created by kimziv on 2017/12/13.
 */

public class LockCoinsPresenter implements LockCoinsContract.Presenter {
    private static final String TAG = LockCoinsFragment.class.getSimpleName();
    private Context context;
    private LockCoinsContract.View view;
    private CompositeSubscription subscriptions;

    public LockCoinsPresenter(Context context, LockCoinsContract.View view) {
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
    public void lockCoins(long amount, long height, String accountPasswd, String secondSecret) {
        String encryptSecret = getAccount().getEncryptSeed();

        Subscription subscription = Observable.create((Observable.OnSubscribe<AschResult>) subscriber -> {
            String secret = Account.decryptSecret(accountPasswd, encryptSecret);
            AschResult result = AschSDK.Account.lockCoins(amount, height,secret,secondSecret);
            if (result.isSuccessful()) {
                subscriber.onNext(result);
                getAccount().getFullAccount().getAccount().setLockHeight(height);
                getAccount().getFullAccount().getAccount().setLocked(true);
                subscriber.onCompleted();
            } else {
                subscriber.onError(new Throwable(result.getError()));
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
                        view.displayLockCoinsResult(true, context.getString(R.string.locked_success));
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void loadBlockInfo() {
        this.view.displayBlockInfo(getAccount());
    }

    @Override
    public void queryLockFee() {
      long fee=  FeeCalculater.calcFee(TransactionType.basic_lock);
      String feeStr =  AppUtil.decimalFormat(AppUtil.decimalFromBigint(fee, AppConstants.PRECISION));
      view.displayLockFee(feeStr+" XAS");
    }

    public Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
