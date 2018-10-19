package asch.so.wallet.contract;

import android.graphics.Bitmap;

import java.util.LinkedHashMap;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.BaseAsset;

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
