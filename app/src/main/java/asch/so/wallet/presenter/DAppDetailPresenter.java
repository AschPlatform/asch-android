package asch.so.wallet.presenter;

import android.content.Context;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.DAppDetailContract;
import asch.so.wallet.miniapp.download.Downloader;
import asch.so.wallet.miniapp.download.DownloadsDB;
import asch.so.wallet.model.entity.DApp;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2018/2/5.
 */

public class DAppDetailPresenter implements DAppDetailContract.Presenter {

    private Context context;
    private DAppDetailContract.View view;
    private CompositeSubscription subscriptions;

    public DAppDetailPresenter(Context context, DAppDetailContract.View view) {
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
        this.subscriptions.clear();
    }

    private DApp queryDappByDAppId(String dappId){
       return DownloadsDB.getImpl().queryDApp(dappId);
    }

    @Override
    public void loadDApp(String dappId) {
        DApp task=queryDappByDAppId(dappId);

        Subscription subscription = Observable.create((Observable.OnSubscribe<DApp>) subscriber -> {
            subscriber.onNext(task);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DApp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                    }

                    @Override
                    public void onNext(DApp dapp) {
                        view.displayDApp(dapp);
                    }
                });
        subscriptions.add(subscription);
    }
}
