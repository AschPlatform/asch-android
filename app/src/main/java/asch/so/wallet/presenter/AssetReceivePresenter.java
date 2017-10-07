package asch.so.wallet.presenter;

import android.content.Context;

import asch.so.wallet.contract.AssetReceiveContract;

/**
 * Created by kimziv on 2017/10/7.
 */

public class AssetReceivePresenter implements AssetReceiveContract.Presenter {

    private  static  final  String TAG=AssetReceivePresenter.class.getSimpleName();
    private  AssetReceiveContract.View view;
    private Context context;

    public AssetReceivePresenter(Context context, AssetReceiveContract.View view) {
        this.context=context;
        this.view=view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void generateQrCode() {

    }
}
