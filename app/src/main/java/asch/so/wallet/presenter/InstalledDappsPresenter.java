package asch.so.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.InstalledDappsContract;
import asch.so.wallet.miniapp.download.TaskModel;
import asch.so.wallet.miniapp.download.TasksDBContraller;
import asch.so.wallet.model.entity.Dapp;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.dto.query.DappQueryParameters;

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

    }

    private List<TaskModel> queryDapps(){
      return   TasksDBContraller.getImpl().getAllTasks();
    }

    @Override
    public void loadInstalledDapps() {
        List<TaskModel> dapps=queryDapps();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<TaskModel>>) subscriber -> {
            subscriber.onNext(dapps);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<TaskModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                    }

                    @Override
                    public void onNext(List<TaskModel> dapps) {
                        view.displayInstalledDapps(dapps);
                    }
                });
        subscriptions.add(subscription);
    }



}
