package asch.so.wallet.contract;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;
import asch.so.wallet.model.entity.UIAAsset;

/**
 * Created by kimziv on 2017/10/7.
 */

public interface AssetReceiveContract {

    interface View extends BaseView<Presenter> {
        void displayQrCode(Bitmap bitmap);
        void displayAssets(List<UIAAsset> assets);
    }

    interface Presenter extends BasePresenter {
        void generateQrCode(String address,String currency, String ammount);
        void loadAssets();
        void saveQrCode(Bitmap bmp);
    }
}
