package asch.so.wallet.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import asch.so.wallet.contract.AssetReceiveContract;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

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
    public void generateQrCode(String content) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(content, BGAQRCodeUtil.dp2px(context, 150), Color.parseColor("#000000"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                   view.displayQrCode(bitmap);
                } else {
                    Toast.makeText(context, "生成英文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
