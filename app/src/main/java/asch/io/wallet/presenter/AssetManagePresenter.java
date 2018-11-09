package asch.io.wallet.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.contract.AssetManageContract;
import asch.io.wallet.model.db.dao.AccountsDao;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.model.entity.Balance;
import asch.io.wallet.model.entity.BaseAsset;
import asch.io.wallet.presenter.component.DaggerPresenterComponent;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AssetManagePresenter implements AssetManageContract.Presenter{
    private final String TAG = AssetManagePresenter.class.getSimpleName();
    private AssetManageContract.View view;

    private CompositeSubscription subscriptions;

    @Inject
    AccountsDao accountsDao;

    List<Balance> allList;


    @Inject
    public AssetManagePresenter(Context context, AssetManageContract.View view){

        this.view=view;
        this.subscriptions=new CompositeSubscription();
        view.setPresenter(this);


//        DaggerPresenterComponent.builder()
//                .applicationModule(new ApplicationModule(context))
//                .build().inject(this);
    }

    @Override
    public void subscribe() {
        loadAllAssets();
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }


    public void loadAllAssets() {

        HashMap<String, BaseAsset> assetHashMap = new HashMap<String, BaseAsset>();
//        assetHashMap = AssetManager.getInstance().;

        List<AschAsset> uiaList = new ArrayList<>();
        List<AschAsset> gatewayList = new ArrayList<>();
        uiaList = AssetManager.getInstance().queryUiaAssets();
        gatewayList = AssetManager.getInstance().queryGatewayAssets();

        view.displayGatewayAssets(gatewayList);
        view.displayUiaAssets(uiaList);

    }






    @Override
    public void saveCurrentAssets(String address) {

    }
}
