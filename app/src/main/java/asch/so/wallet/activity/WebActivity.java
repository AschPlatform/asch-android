package asch.so.wallet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.just.library.AgentWeb;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.fragment.WebFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/16.
 */

public class WebActivity extends TitleToolbarActivity {

//    private AgentWeb agentWeb;
//    @BindView(R.id.root_ll)
//    LinearLayout linearLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("使用手册");
        setRightTitle("打开浏览器");
        WebFragment fragment=WebFragment.newInstance(AppConstants.USER_MANNUAL_URL);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

        StatusBarUtil.immersive(this);

    }

    @Override
    protected void onRightClicked(View v) {
        //super.onRightClicked(v);
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(AppConstants.USER_MANNUAL_URL);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
