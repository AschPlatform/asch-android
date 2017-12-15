package asch.so.wallet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import asch.so.base.activity.ActivityStackManager;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.SecretBackupFragment;

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
        setTitle("助记词备份");
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
        builder.setTitle("重要提示")
                .setMessage("请您确认：助记词是否已经备份到安全的地方？备份的机会只有一次，若已经备份，请点击\"确定\"；若还没有备份，请点击\"取消\"重新进行备份！")
                .setCancelable(false)
                .setPositiveButton("确定",okListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
