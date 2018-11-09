package asch.io.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

import asch.io.base.view.Throwable;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.MainContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.FullAccount;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;

/**
 * Created by kimziv on 2017/11/15.
 */

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG=MainPresenter.class.getSimpleName();

    private MainContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;

    public MainPresenter(Context context, MainContract.View view){
        this.context=context;
        this.view=view;
        subscriptions=new CompositeSubscription();
        view.setPresenter(this);
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        this.subscriptions.clear();
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void loadFullAccount() {
        String publicKey=getAccount().getPublicKey();

        rx.Observable loginObservable = rx.Observable.create(new rx.Observable.OnSubscribe<FullAccount>() {
            @Override
            public void call(Subscriber<? super FullAccount> subscriber) {
                try {
                    String address= AschFactory.getInstance().getSecurity().getAddress(publicKey);
                    AschResult result = AschSDK.Account.getAccountV2(address);
                    if (result!=null && result.isSuccessful()){
                        LogUtils.iTag(TAG,result.getRawJson());
//                        Map<String, Object> map =result.parseMap();
                        FullAccount account= JSON.parseObject(result.getRawJson(),FullAccount.class);
                        subscriber.onNext(account);
                        subscriber.onCompleted();
                    }else {
                        subscriber.onError(result.getException());
                    }
                }
                catch (Exception ex){
                    subscriber.onError(ex);
                }
            }
        });
       Subscription subscription= loginObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FullAccount>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        LogUtils.dTag("loginObservable error:",e.toString());
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                    }

                    @Override
                    public void onNext(FullAccount account) {
                        getAccount().setFullAccount(account);
                       long blockHeight = getAccount().getFullAccount().getLatestBlock().getHeight();
                        //Toast.makeText(context,"blockHeight:"+blockHeight, Toast.LENGTH_SHORT).show();
                    }
                });
       subscriptions.add(subscription);
    }
}
