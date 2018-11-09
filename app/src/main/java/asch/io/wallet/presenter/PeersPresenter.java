package asch.io.wallet.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import asch.io.base.adapter.page.IPage;
import asch.io.base.adapter.page.Page1;
import asch.io.base.view.Throwable;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.contract.PeesContact;
import asch.io.wallet.model.entity.PeerNode;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.dto.query.PeerQueryParameters;

/**
 * Created by kimziv on 2017/11/24.
 */

public class PeersPresenter implements PeesContact.Presenter {

    private Context context;
    private PeesContact.View view;
    private IPage pager;
    private CompositeSubscription subscriptions;
    public PeersPresenter(Context context, PeesContact.View view) {
        this.context = context;
        this.view = view;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);
        initPager();
    }

    private void initPager(){
        // pageIndex, pageSize策略
        this.pager = new Page1() {
            @Override
            public void load(int param1, int param2) {
                loadPeers(param1, param2);
            }
        };
        this.pager.setStartPageIndex(0);
        this.pager.setPageSize(AppConstants.DEFAULT_PAGE_SIZE);
    }

    private void loadPeers(int pageIndex, int pageSize) {
        int offset = pageIndex * pageSize;
        int limit = pageSize;
        //String address = getAccount().getAddress();
      Subscription subscription = Observable.create((Observable.OnSubscribe<List<PeerNode>>) subscriber -> {
            PeerQueryParameters params = new PeerQueryParameters()
                    //.orderByDescending("t_timestamp")
                    .setOffset(offset)
                    .setLimit(limit);
            AschResult result = AschSDK.Peer.queryPeers(params);
            if (result.isSuccessful()) {
                JSONObject resultJSONObj = JSONObject.parseObject(result.getRawJson());
                JSONArray peersJsonArray = resultJSONObj.getJSONArray("peers");
                List<PeerNode> peers = JSON.parseArray(peersJsonArray.toJSONString(), PeerNode.class);
                subscriber.onNext(peers);
                subscriber.onCompleted();
            } else {
                subscriber.onError(result.getException());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<PeerNode>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        view.displayError(new Throwable(context.getString(R.string.net_error)));
                        pager.finishLoad(true);
                    }

                    @Override
                    public void onNext(List<PeerNode> peers) {
                        if (pager.isFirstPage()) {
                            view.displayFirstPagePeers(peers);
                        } else {
                            view.displayMorePagePeers(peers);
                        }
                        pager.finishLoad(true);
                    }
                });
      subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadFirstPagePeers() {
        pager.loadPage(true);
    }

    @Override
    public void loadMorePagePeers() {
        pager.loadPage(false);
    }
}
