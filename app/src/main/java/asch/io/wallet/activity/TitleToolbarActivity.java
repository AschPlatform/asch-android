package asch.io.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import asch.io.base.activity.BaseActivity;
import asch.io.wallet.R;
import asch.io.wallet.util.StatusBarUtil;
import asch.io.widget.toolbar.BaseToolbar;
import asch.io.widget.toolbar.TitleToolbar;

/**
 * Created by kimziv on 2017/10/25.
 */

public class TitleToolbarActivity extends BaseActivity {

    TitleToolbar toolbar=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base);
        setContentView(activityLayoutResId());
        initToolBar();
//        setOrientation();
        StatusBarUtil.immersive(this);
    }

    protected  int activityLayoutResId(){
        return R.layout.activity_base;
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
                    case R.id.right:
                        onRightClicked(v);
                        break;
                }
            }
        });
        setSupportActionBar(toolbar);
        setBackTitle(getString(R.string.back));
    }

    protected void setBackTitle(String title){
        toolbar.setBackText(title==null?getString(R.string.back):title);
        toolbar.setBackVisible(true);
    }

    protected void setTitle(String title){
        toolbar.setTitle(title);
        toolbar.setTitleVisible(true);
    }

//    protected void setOrientation(){
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//    }

    protected void setRightTitle(String title){
        toolbar.setRightText(title);
        toolbar.setRightVisible(true);
    }

    /**
     * 返回
     * @param v
     */
    protected void onBackClicked(View v){
        onBackPressed();
    }

    /**
     * 需要重写
     * @param v
     */
    protected void onRightClicked(View v) {

    }
}
