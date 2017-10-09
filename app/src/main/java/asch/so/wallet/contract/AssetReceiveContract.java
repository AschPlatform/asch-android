package asch.so.wallet.contract;

import android.content.Context;
import android.graphics.Bitmap;

import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;

/**
 * Created by kimziv on 2017/10/7.
 */

public interface AssetReceiveContract {

    interface View extends BaseView<Presenter> {
        void displayQrCode(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter {
        void generateQrCode(String address,String currency, String ammount);
        void testDecodeQRCodeURL();
    }
}
