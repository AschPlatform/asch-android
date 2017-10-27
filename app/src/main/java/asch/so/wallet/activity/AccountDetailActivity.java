package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.fragment.AccountDetailFragment;
import asch.so.widget.toolbar.BaseToolbar;
import asch.so.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/27.
 */

public class AccountDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    TitleToolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_detail);
        ButterKnife.bind(this);
       // AccountDetailFragment fragment = AccountDetailFragment.newInstance();
        //ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
        StatusBarUtil.immersive(this);
        initToolBar();
    }

    private void initToolBar(){
        //toolbar = (TitleToolbar) findViewById(R.id.toolbar);
        toolbar.setCloseVisible(false);
        toolbar.setOnOptionItemClickListener(new BaseToolbar.OnOptionItemClickListener() {
            @Override
            public void onOptionItemClick(View v) {
                switch (v.getId()){
                    case R.id.back:
                       onBackPressed();
                        break;
                }
            }
        });
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.item_save:
            {
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
