package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.just.library.AgentWeb;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/27.
 */

public class WebFragment extends BaseFragment {

    private AgentWeb agentWeb;
    @BindView(R.id.root_ll)
    LinearLayout linearLayout;

    public static WebFragment newInstance() {
        
        Bundle args = new Bundle();
        
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_webview,container,false);
        ButterKnife.bind(this,rootView);
        agentWeb = AgentWeb.with(this)//传入Activity or Fragment
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                //.setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go("http://aschd.org/tx/05b99fd4680440fe4d24cbc121b130cba61866c408e29803b4dbbd996f3d9276/");

        return rootView;
    }
}