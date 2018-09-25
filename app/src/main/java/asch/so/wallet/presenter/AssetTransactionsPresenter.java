package asch.so.wallet.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.adapter.page.IPage;
import asch.so.base.adapter.page.Page1;
import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.TransferAsset;
import asch.so.wallet.model.entity.UIATransferAsset;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.TransactionType;
import so.asch.sdk.dto.query.TransactionQueryParameters;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/10/13.
 */

public class AssetTransactionsPresenter implements AssetTransactionsContract.Presenter {

    private AssetTransactionsContract.View view;
    private Context context;
    private CompositeSubscription subscriptions;
    IPage pager;

    public AssetTransactionsPresenter(Context ctx, AssetTransactionsContract.View view) {
        this.view = view;
        this.context = ctx;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);
    }

    @Override
    public void subscribe() { }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }



}
