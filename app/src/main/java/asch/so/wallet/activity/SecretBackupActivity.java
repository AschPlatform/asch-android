package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        BackupFromInApp(2);

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
        if (action==Action.BackupFromStart){
            Intent intent =new Intent(this, MainTabActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().finishAll();
        }else if (action==Action.BackupFromInApp){
            ActivityStackManager.getInstance().finishNActivity(2);
        }
    }
}
