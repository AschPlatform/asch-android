package asch.io.wallet.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import asch.io.base.view.Throwable;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.BlockInfoContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.FullAccount;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2017/12/1.
 */

public class BlockInfoPresenter implements BlockInfoContract.Presenter {

    private Context context;
    private BlockInfoContract.View view;
    private CompositeSubscription subscriptions;

    public BlockInfoPresenter(Context context, BlockInfoContract.View view) {
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

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void loadBlockInfo() {
        String publicKey = getAccount().getPublicKey();
        rx.Observable<FullAccount> observable = AccountsManager.getInstance().createLoginAccountObservable(publicKey);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FullAccount>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        LogUtils.dTag("xasObservable error:", e.toString());
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                    }

                    @Override
                    public void onNext(FullAccount fullAccount) {
                        view.displayBlockInfo(fullAccount);
                    }
                });
        subscriptions.add(subscription);
    }
}
