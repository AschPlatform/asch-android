package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import asch.so.base.activity.BaseActivity;
import asch.so.wallet.R;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.widget.toolbar.BaseToolbar;
import asch.so.widget.toolbar.TitleToolbar;

/**
 * Created by kimziv on 2017/10/25.
 */

public class TitleToolbarActivity extends BaseActivity {

    TitleToolbar toolbar=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initToolBar();
        StatusBarUtil.immersive(this);
    }

    protected void initToolBar(){
        toolbar = (TitleToolbar) findViewById(R.id.toolbar);
        toolbar.setCloseVisible(false);
        toolbar.setOnOptionItemClickListener(new BaseToolbar.OnOptionItemClickListener() {
            @Override
            public void onOptionItemClick(View v) {
                switch (v.getId()){
                    case R.id.back:
                       onBackClicked(v);
                        break;
                }
            }
        });
        setSupportActionBar(toolbar);
        setBackTitle("返回");
    }

    protected void setBackTitle(String title){
        toolbar.setBackText(title==null?"返回":title);
        toolbar.setBackVisible(true);
    }

    protected void setTitle(String title){
        toolbar.setTitle(title);
        toolbar.setTitleVisible(true);
    }

    /**
     * 返回
     * @param v
     */
    protected void onBackClicked(View v){
        onBackPressed();
    }
}
