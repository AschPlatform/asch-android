package asch.io.wallet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.fragment.AssetWithdrawFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//import com.j256.ormlite.stmt.query.In;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetWithdrawActivity extends TitleToolbarActivity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private AssetWithdrawFragment fragment;

    //        fragment.setPresenter(presenter);
    public enum Action{
        ScanSecretToTransfer(1),
        AssetBalanceToTransfer(2);

        public int value;
        Action(int value) {
            this.value = value;
        }

        public static Action valueOf(int value) {
            switch (value) {
                case 1:
                    return ScanSecretToTransfer;
                case 2:
                    return AssetBalanceToTransfer;
                default:
                    return null;
            }
        }

        public int getValue() {
            return value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.withdraw));
        fragment=AssetWithdrawFragment.newInstance();
        Bundle bundle=getIntent().getExtras();
        fragment.setArguments(bundle);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }




    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            String qrDecodeString = data.getStringExtra("QRDecodeString");

            if (fragment != null) {
                fragment.setTargetAddress(qrDecodeString);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }


    /**
     * 请求相机和闪光灯权限
     */
    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.photo_permission), REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
}
