package asch.so.wallet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.j256.ormlite.stmt.query.In;

import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.presenter.AssetTransferPresenter;
import asch.so.wallet.presenter.AssetBalancePresenter;
import asch.so.wallet.view.fragment.AssetTransactionsFragment;
import asch.so.wallet.view.fragment.AssetTransferFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransferActivity extends TitleToolbarActivity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    private AssetTransferPresenter presenter;
    private AssetTransferFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
        setTitle("资产转账");
        fragment=AssetTransferFragment.newInstance();

        Bundle bundle=getIntent().getExtras();
        fragment.setArguments(bundle);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
        presenter =new AssetTransferPresenter(this,fragment);
        fragment.setPresenter(presenter);
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

            Toast.makeText(this,qrDecodeString,Toast.LENGTH_SHORT).show();
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
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
}
