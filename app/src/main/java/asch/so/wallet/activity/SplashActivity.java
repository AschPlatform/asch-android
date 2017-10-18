package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import asch.so.base.activity.BaseActivity;
import asch.so.wallet.R;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.widget.particleview.ParticleView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/10/13.
 */

public class SplashActivity extends BaseActivity {


   // @BindView(R.id.root_ll)
   // LinearLayout rootLl;
    @BindView(R.id.splash_pv)
    ParticleView particleView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.immersive(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//                particleView.setFitsSystemWindows(true);
//                //particleView.setClipToPadding(false);
//            }
//        }

        particleView.startAnim();
        particleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);
                //Intent intent = new Intent(SplashActivity.this, FirstStartActivity.class);
                SplashActivity.this.startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
