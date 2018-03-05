package asch.so.wallet.presenter;

import android.content.Context;

import java.util.List;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.InstalledDappsContract;
import asch.so.wallet.miniapp.download.DownloadsDB;
import asch.so.wallet.model.entity.DApp;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2018/1/24.
 */

public class InstalledDappsPresenter implements InstalledDappsContract.Presenter {
    private Context context;
    private InstalledDappsContract.View view;
    private CompositeSubscription subscriptions;

    public InstalledDappsPresenter(Context context, InstalledDappsContract.View view) {
        this.context = context;
        this.view = view;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);
    }



    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        this.subscriptions.clear();
    }

    private List<DApp> queryDapps(){
      return  DownloadsDB.getImpl().queryAllTasks();
    }

    @Override
    public void loadInstalledDapps() {
        List<DApp> dapps=queryDapps();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<DApp>>) subscriber -> {
            subscriber.onNext(dapps);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DApp>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                    }

                    @Override
                    public void onNext(List<DApp> dapps) {
                        view.displayInstalledDapps(dapps);
                    }
                });
        subscriptions.add(subscription);
    }



}
