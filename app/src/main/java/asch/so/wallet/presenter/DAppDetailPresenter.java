package asch.so.wallet.presenter;

import android.content.Context;

import java.util.List;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.DAppDetailContract;
import asch.so.wallet.miniapp.download.TaskModel;
import asch.so.wallet.miniapp.download.TasksDBContraller;
import asch.so.wallet.model.entity.Dapp;
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

    private TaskModel queryDappByDAppId(String dappId){
       return TasksDBContraller.getImpl().getTaskByDappId(dappId);
    }

    @Override
    public void loadDApp(String dappId) {
       TaskModel task=queryDappByDAppId(dappId);

        Subscription subscription = Observable.create((Observable.OnSubscribe<TaskModel>) subscriber -> {
            subscriber.onNext(task);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TaskModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                    }

                    @Override
                    public void onNext(TaskModel task) {
                        view.displayDApp(task);
                    }
                });
        subscriptions.add(subscription);
    }
}
