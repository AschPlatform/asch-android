package asch.so.wallet.presenter;

import android.content.Context;
import android.graphics.Color;

import asch.so.wallet.contract.DappContainerContract;

/**
 * Created by kimziv on 2017/10/11.
 */

public class DappContainerPresenter implements DappContainerContract.Presenter {

    private DappContainerContract.View view;
    private Context context;

    public DappContainerPresenter(Context context, DappContainerContract.View view) {
        this.view = view;
        this.context = context;
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }


    @Override
    public void unSubscribe() {

    }

    @Override
    public void loadDapp() {

    }
}
