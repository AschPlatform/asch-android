package asch.io.wallet.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.franmontiel.localechanger.LocaleChanger;

import java.util.Locale;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.util.StatusBarUtil;
import asch.io.wallet.view.fragment.WebFragment;

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
        setTitle(getString(R.string.use_explain));
        setRightTitle(getString(R.string.open_browser));

        WebFragment fragment=WebFragment.newInstance(getMannualUrl());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);

        StatusBarUtil.immersive(this);

    }

    @Override
    protected void onRightClicked(View v) {
        //super.onRightClicked(v);
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(getMannualUrl());
        intent.setData(uri);
        startActivity(intent);
    }

    private String getMannualUrl(){
        if (AppConstants.SUPPORTED_LOCALES.get(2).getLanguage().equals(LocaleChanger.getLocale().getLanguage())||
                AppConstants.SUPPORTED_LOCALES.get(2).getLanguage().equals(Locale.getDefault())){
            return AppConstants.USER_MANNUAL_URL_EN;
        }else{
          return   AppConstants.USER_MANNUAL_URL;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
