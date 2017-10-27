package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.just.library.AgentWeb;

import asch.so.base.activity.BaseActivity;
import asch.so.base.util.ActivityUtils;
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
       // setContentView(R.layout.activity_webview);
       // ButterKnife.bind(this);
//        agentWeb = AgentWeb.with(this)//传入Activity or Fragment
//                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
//                .useDefaultIndicator()// 使用默认进度条
//                .defaultProgressBarColor() // 使用默认进度条颜色
//                //.setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
//                .createAgentWeb()//
//                .ready()
//                .go("http://aschd.org/tx/05b99fd4680440fe4d24cbc121b130cba61866c408e29803b4dbbd996f3d9276/");

        setTitle("交易详情");
        WebFragment fragment=WebFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

        StatusBarUtil.immersive(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
