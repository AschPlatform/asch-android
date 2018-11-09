package asch.io.wallet.contract;

import android.graphics.Bitmap;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;

/**
 * Created by kimziv on 2017/10/7.
 */

public interface AssetGatewayDepositContract {

    interface View extends BaseView<Presenter> {
        void displayQrCode(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter {
        void generateQrCode(String address);
        void loadAssets(boolean ignoreCache);
        void saveQrCode(Bitmap bmp);
    }
}
