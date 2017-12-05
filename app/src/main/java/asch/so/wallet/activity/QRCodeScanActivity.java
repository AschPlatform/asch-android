package asch.so.wallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import asch.so.base.activity.BaseActivity;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.QRCodeURL;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.validator.Validator;
import asch.so.widget.toolbar.BaseToolbar;
import asch.so.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;


/**
 * Created by kimziv on 2017/9/22.
 */

public class QRCodeScanActivity extends TitleToolbarActivity implements QRCodeView.Delegate{

    private static final String TAG = QRCodeScanActivity.class.getSimpleName();
   // private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    public enum Action{
       ScanSecretToPaste(1),
       ScanAddressToPaste(2),
       ScanAddressToPay(3);

       public int value;
       Action(int value) {
           this.value = value;
       }

       public static Action valueOf(int value) {
           switch (value) {
               case 1:
                   return ScanSecretToPaste;
               case 2:
                   return ScanAddressToPaste;
               case 3:
                   return ScanAddressToPay;
               default:
                   return null;
           }
       }

       public int getValue() {
           return value;
       }
   }

    @BindView(R.id.toolbar)
    TitleToolbar toolbar;

    @BindView(R.id.zbarview)
    ZBarView zbarView;

    private Action action;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int act= bundle.getInt("action");
        action = Action.valueOf(act);

        setContentView(R.layout.activity_qrcode_scan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("扫一扫");
        toolbar.setOnOptionItemClickListener(new BaseToolbar.OnOptionItemClickListener() {
            @Override
            public void onOptionItemClick(View v) {
                if (v.getId()==R.id.back){
                    onBackPressed();
                }
            }
        });
        StatusBarUtil.immersive(this);
        zbarView.setDelegate(this);

//        zxingView.startSpot();
    }

    @Override
    protected void onStart() {
        super.onStart();
        zbarView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        zbarView.showScanRect();
        zbarView.startSpot();
    }

    @Override
    protected void onStop() {
        zbarView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zbarView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        vibrate();
       // zbarView.startSpot();
        switch (action){
            case ScanSecretToPaste:
            {
                Intent intent = new Intent();
                intent.putExtra("QRDecodeString", result);
                setResult(RESULT_OK, intent);
                finish();
            }
                break;
            case ScanAddressToPaste:
            {
                if (!Validator.check(this, Validator.Type.QRCodeUrl,result,"无效地址"))
                {
                    zbarView.startSpot();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("QRDecodeString", result);
                setResult(RESULT_OK, intent);
                finish();
            }
                break;
            case ScanAddressToPay:
            {
                if (!Validator.check(this, Validator.Type.QRCodeUrl,result,"无效地址"))
                {
                    zbarView.startSpot();
                    return;
                }
                Bundle bundle =new Bundle();
                try {
                    //QRCodeURL uri = QRCodeURL.decodeQRCodeURL(result);
                    bundle.putString("qrcode_uri",result);
                    bundle.putInt("action", AssetTransferActivity.Action.ScanSecretToTransfer.getValue());
                    BaseActivity.start(this,AssetTransferActivity.class,bundle);
                    finish();
                }catch (Exception e){
                    AppUtil.toastError(this,"二维码格式无效");
                }
            }
                break;
        }
        AppUtil.toastSuccess(this, result);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        //Log.e(TAG, "打开相机出错");
        AppUtil.toastError(this,"打开相机出错");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
//            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
//
//            /*
//            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
//            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
//             */
//            new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    if (TextUtils.isEmpty(result)) {
//                        Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }.execute();
//        }
    }
}
