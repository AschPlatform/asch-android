package asch.io.wallet.contract;

import android.graphics.Bitmap;

import java.util.LinkedHashMap;

import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;
import asch.io.wallet.model.entity.BaseAsset;

/**
 * Created by kimziv on 2017/10/7.
 */

public interface AssetReceiveContract {

    interface View extends BaseView<Presenter> {
        void displayQrCode(Bitmap bitmap);
        void displayAssets(LinkedHashMap<String,BaseAsset> assetsMap);
    }

    interface Presenter extends BasePresenter {
        void generateQrCode(String address,String currency, String ammount);
        void generateQrCode(String address);
        void loadAssets(boolean ignoreCache);
        void saveQrCode(Bitmap bmp);
    }
}
