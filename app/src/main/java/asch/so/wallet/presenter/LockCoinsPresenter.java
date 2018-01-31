package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.LockCoinsContract;
import asch.so.wallet.model.entity.Delegate;
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
    public void lockCoins(long height, String secret, String secondSecret) {

        Subscription subscription = Observable.create((Observable.OnSubscribe<AschResult>) subscriber -> {
//            AschResult result = AschSDK.Account.lockCoins(height,secret,secondSecret);
            AschResult result = AschSDK.Signature.setSignature(secret,secondSecret,null,null);
            if (result.isSuccessful()) {
                subscriber.onNext(result);
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
                        LogUtils.dTag(TAG,"vote result:"+e==null?"vote result error":e.toString());
                        view.displayLockCoinsResult(false,e==null?context.getString(R.string.locked_fail):e.toString());
                    }

                    @Override
                    public void onNext(AschResult result) {
                        LogUtils.dTag(TAG,"vote result:"+result.getRawJson());
                        view.displayLockCoinsResult(true, context.getString(R.string.locked_success));
                    }
                });
        subscriptions.add(subscription);
    }


}
