package asch.so.wallet.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;

import asch.so.base.view.Throwable;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.MainContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.FullAccount;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

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
                    AschResult result = AschSDK.Account.secureLogin(publicKey);
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
                        view.displayError(new Throwable("登录接口网络错误"));
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
