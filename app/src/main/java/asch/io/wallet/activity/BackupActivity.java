package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.io.wallet.R;

/**
 * Created by kimziv on 2017/10/25.
 */

public class BackupActivity extends TitleToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.backup_account));
    }
}
