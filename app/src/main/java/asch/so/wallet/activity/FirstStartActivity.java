package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import asch.so.base.activity.BaseActivity;
import asch.so.wallet.R;

/**
 * Created by kimziv on 2017/10/16.
 * 第一次创建钱包的页面
 */

public class FirstStartActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
