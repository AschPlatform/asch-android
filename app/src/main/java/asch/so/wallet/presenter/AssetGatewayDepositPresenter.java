package asch.so.wallet.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import asch.so.wallet.R;
import asch.so.wallet.contract.AssetGatewayDepositContract;
import asch.so.wallet.model.entity.QRCodeURL;
import asch.so.wallet.util.AppUtil;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kimziv on 2017/10/7.
 */

public class AssetGatewayDepositPresenter implements AssetGatewayDepositContract.Presenter {

    private  static  final  String TAG=AssetGatewayDepositPresenter.class.getSimpleName();
    private  AssetGatewayDepositContract.View view;
    private Context context;
    private QRCodeURL qrCodeURL;
    private CompositeSubscription subscriptions;

    Handler handler;

    public AssetGatewayDepositPresenter(Context context, AssetGatewayDepositContract.View view) {
        this.context=context;
        this.view=view;
        this.qrCodeURL=new QRCodeURL();
        view.setPresenter(this);
        subscriptions=new CompositeSubscription();
        this.handler=new Handler(context.getMainLooper()){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==1){
                    AppUtil.toastSuccess(context,context.getString(R.string.saved_phone));
                }else {
                    AppUtil.toastError(context,context.getString(R.string.save_error));
                }
            }
        };

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }


    @Override
    public void generateQrCode(String address) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {

                return QRCodeEncoder.syncEncodeQRCode(address, BGAQRCodeUtil.dp2px(context, 150), Color.parseColor("#000000"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    view.displayQrCode(bitmap);
                } else {
                    AppUtil.toastError(context, context.getString(R.string.create_qr_fail));
                }
            }
        }.execute();
    }


    private String buildQRCodeURL(String address,String currency, String ammount){
        qrCodeURL.setAddress(address);
        qrCodeURL.setCurrency(currency);
        qrCodeURL.setAmount(ammount);
       return qrCodeURL.encodeQRCodeURL();
    }


    @Override
    public void loadAssets(boolean ignoreCache) {
        //TODO
//        view.displayAssets(AssetManager.getInstance().getAllssets());
    }



    @Override
    public void saveQrCode(Bitmap bmp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = saveImageToGallery(context,bmp);
                handler.sendEmptyMessage(result?1:0);
            }
        }).start();
    }

    //保存文件到指定路径
    public  boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片

        //String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "asch";
        String storePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + File.separator + "asch";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
         boolean isSuccess=false;
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = simpleDate.format(now.getTime())+".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

}
