package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.LockCoinsContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.model.entity.FullAccount;
import asch.so.wallet.view.fragment.LockCoinsFragment;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

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
    public void lockCoins(long height, String accountPasswd, String secondSecret) {
        String encryptSecret = getAccount().getEncryptSeed();

        Subscription subscription = Observable.create((Observable.OnSubscribe<AschResult>) subscriber -> {
            String secret = Account.decryptSecret(accountPasswd, encryptSecret);
            AschResult result = AschSDK.Account.lockCoins(height,secret,secondSecret);
            if (result.isSuccessful()) {
                subscriber.onNext(result);
                getAccount().getFullAccount().getAccount().setLockHeight(height);
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


    public Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }
}
