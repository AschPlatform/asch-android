package asch.so.wallet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

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
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by kimziv on 2017/9/22.
 */

public class QRCodeScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private static final String TAG = QRCodeScanActivity.class.getSimpleName();

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 100;
    public enum Action {
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
        int act = bundle.getInt("action");
        action = Action.valueOf(act);

        setContentView(R.layout.activity_qrcode_scan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("扫一扫");
        toolbar.setRightVisible(true);
        toolbar.setRightText("相册");
        toolbar.setOnOptionItemClickListener(new BaseToolbar.OnOptionItemClickListener() {
            @Override
            public void onOptionItemClick(View v) {
                if (v.getId() == R.id.back) {
                    onBackPressed();
                }else if (v.getId() == R.id.right){
                    chooseGallery();
                }
            }
        });

        StatusBarUtil.immersive(this);
        zbarView.setDelegate(this);

//        zxingView.startSpot();
    }

    @AfterPermissionGranted(REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY)
    private void chooseGallery() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .maxChooseCount(1) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
//            PhotoPicker.builder()
//                    .setPhotoCount(1)
//                    .setGridColumnCount(3)
//                    .setShowCamera(false)
//                    .setShowGif(false)
//                    .setPreviewEnabled(true)
//                    .start(this, PhotoPicker.REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY, perms);
        }
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
        switch (action) {
            case ScanSecretToPaste: {
                Intent intent = new Intent();
                intent.putExtra("QRDecodeString", result);
                setResult(RESULT_OK, intent);
                finish();
            }
            break;
            case ScanAddressToPaste: {
                if (!Validator.check(this, Validator.Type.QRCodeUrl, result, "无效地址")) {
                    zbarView.startSpot();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("QRDecodeString", result);
                setResult(RESULT_OK, intent);
                finish();
            }
            break;
            case ScanAddressToPay: {
                if (!Validator.check(this, Validator.Type.QRCodeUrl, result, "无效地址")) {
                    zbarView.startSpot();
                    return;
                }
                Bundle bundle = new Bundle();
                try {
                    //QRCodeURL uri = QRCodeURL.decodeQRCodeURL(result);
                    bundle.putString("qrcode_uri", result);
                    bundle.putInt("action", AssetTransferActivity.Action.ScanSecretToTransfer.getValue());
                    BaseActivity.start(this, AssetTransferActivity.class, bundle);
                    finish();
                } catch (Exception e) {
                    AppUtil.toastError(this, "二维码格式无效");
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
        AppUtil.toastError(this, "打开相机出错");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode ==REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY  && data != null) {
            List<String> selectedPhotos = BGAPhotoPickerActivity.getSelectedPhotos(data);
            String imagePath=selectedPhotos.get(0);

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(imagePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        AppUtil.toastError(QRCodeScanActivity.this, "未发现二维码");
                    } else {
                        AppUtil.toastSuccess(QRCodeScanActivity.this, result);
                    }
                }
            }.execute();
        }

    }
}
