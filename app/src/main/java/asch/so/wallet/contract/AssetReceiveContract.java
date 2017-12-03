package asch.so.wallet.contract;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.UIAAsset;

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
        void loadAssets();
        void saveQrCode(Bitmap bmp);
    }
}
