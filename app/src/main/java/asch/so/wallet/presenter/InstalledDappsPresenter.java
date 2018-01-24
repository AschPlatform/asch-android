package asch.so.wallet.presenter;

import android.content.Context;

import asch.so.wallet.contract.InstalledDappsContract;

/**
 * Created by kimziv on 2018/1/24.
 */

public class InstalledDappsPresenter implements InstalledDappsContract.Presenter {
    private Context context;
    private InstalledDappsContract.View view;

    public InstalledDappsPresenter(Context context, InstalledDappsContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
    }

    

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void loadInstalledDapps() {

    }


}
