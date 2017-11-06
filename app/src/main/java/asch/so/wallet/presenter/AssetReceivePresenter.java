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
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import asch.so.wallet.TestData;
import asch.so.wallet.contract.AssetReceiveContract;
import asch.so.wallet.model.entity.QRCodeURL;
import asch.so.wallet.model.entity.UIAAsset;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/10/7.
 */

public class AssetReceivePresenter implements AssetReceiveContract.Presenter {

    private  static  final  String TAG=AssetReceivePresenter.class.getSimpleName();
    private  AssetReceiveContract.View view;
    private Context context;

    private QRCodeURL qrCodeURL;

    public AssetReceivePresenter(Context context, AssetReceiveContract.View view) {
        this.context=context;
        this.view=view;
        this.qrCodeURL=new QRCodeURL();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
   public void generateQrCode(String address,String currency, String ammount) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                String qrcodeURL=buildQRCodeURL(address, currency,ammount);
                return QRCodeEncoder.syncEncodeQRCode(qrcodeURL, BGAQRCodeUtil.dp2px(context, 150), Color.parseColor("#000000"));
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

    private String buildQRCodeURL(String address,String currency, String ammount){
        qrCodeURL.setAddress(address);
        qrCodeURL.setCurrency(currency);
        qrCodeURL.setAmount(ammount);
       return qrCodeURL.encodeQRCodeURL();
    }

    @Override
    public  void testDecodeQRCodeURL(){
        try {
            QRCodeURL url =  QRCodeURL.decodeQRCodeURL(qrCodeURL.encodeQRCodeURL());
            Toast.makeText(context,url.toString(),Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadAssets() {
        ArrayList<UIAAsset> list=new ArrayList<UIAAsset>();
        Observable uiaOervable =
                Observable.create(new Observable.OnSubscribe<List<UIAAsset>>(){
                    @Override
                    public void call(Subscriber<? super List<UIAAsset>> subscriber) {
                        AschResult result = AschSDK.UIA.getAssets(100,0);
                        Log.i(TAG,result.getRawJson());
                        if (result.isSuccessful()){
                            JSONObject resultJSONObj=JSONObject.parseObject(result.getRawJson());
                            JSONArray balanceJsonArray=resultJSONObj.getJSONArray("assets");
                            List<UIAAsset> assets= JSON.parseArray(balanceJsonArray.toJSONString(),UIAAsset.class);
                            list.addAll(assets);
                            subscriber.onNext(list);
                            subscriber.onCompleted();
                        }else{
                            subscriber.onError(result.getException());
                        }
                    }
                });

        uiaOervable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<List<UIAAsset>>() {
                    @Override
                    public void call(List<UIAAsset> assets) {
                        view.displayAssets(assets);
                    }
                });
    }

    @Override
    public void saveQrCode(Bitmap bmp) {
        Handler handler=new Handler(context.getMainLooper()){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==1){
                    Toast.makeText(context,"已保存到相册",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"保存失败",Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = saveImageToGallery(context,bmp);
                handler.sendEmptyMessage(result?1:0);
            }
        }).start();
    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片

        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "asch";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
//        String fileName = System.currentTimeMillis() + ".jpg";
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = simpleDate.format(now.getTime());
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
