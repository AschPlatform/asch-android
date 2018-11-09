package asch.io.wallet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import asch.io.base.activity.ActivityStackManager;
import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.SecretBackupFragment;

/**
 * Created by kimziv on 2017/10/31.
 */

public class SecretBackupActivity extends TitleToolbarActivity {

    private Action action;

    public enum Action {
        BackupFromStart(1),
        BackupFromInApp(2),
        BackupFromAccountDetail(3),
        ;

        public int value;

        Action(int value) {
            this.value = value;
        }

        public static Action valueOf(int value) {
            switch (value) {
                case 1:
                    return BackupFromStart;
                case 2:
                    return BackupFromInApp;
                case 3:
                    return BackupFromAccountDetail;
                default:
                    return null;
            }
        }

        public int getValue() {
            return value;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.mnemonic_backup));
        SecretBackupFragment fragment = SecretBackupFragment.newInstance();
        action=Action.valueOf(getBundle().getInt("action"));
        fragment.setArguments(getBundle());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
    }

    @Override
    protected void onBackClicked(View v) {
        showBackupAlertDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goback(v);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.onBackClicked(null);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void goback(View v){
        if (action==Action.BackupFromStart){
            Intent intent =new Intent(this, MainTabActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().finishAll();
        }else if (action==Action.BackupFromInApp){
            ActivityStackManager.getInstance().finishNActivity(2);
        }else if(action==Action.BackupFromAccountDetail){
            super.onBackClicked(v);
        }
    }



    private void showBackupAlertDialog(DialogInterface.OnClickListener okListener){
        AlertDialog.Builder builder =new AlertDialog
                .Builder(this);
        builder.setTitle(getString(R.string.important_note))
                .setMessage(getString(R.string.sure_backup))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.confirm),okListener)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
