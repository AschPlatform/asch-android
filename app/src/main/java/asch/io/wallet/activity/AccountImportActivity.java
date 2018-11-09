package asch.io.wallet.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.AccountImportFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by kimziv on 2017/9/22.
 */

public class AccountImportActivity extends TitleToolbarActivity implements  EasyPermissions.PermissionCallbacks{
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    public static final int REQUEST_CODE_FROM_ACCOUNTS =2;
    public static final int REQUEST_CODE_FROM_START =3;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AccountImportFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(getString(R.string.import_account));
        fragment = AccountImportFragment.newInstance();
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_import, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.item_scan_qrcode:
            {
                Intent intent =new Intent(this, QRCodeScanActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("action", QRCodeScanActivity.Action.ScanSecretToPaste.value);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK){
            String qrDecodeString=data.getStringExtra("QRDecodeString");
            if (fragment!=null){
                fragment.setSeed(qrDecodeString);
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
