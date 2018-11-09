package asch.io.wallet.activity;

import android.content.Intent;
import android.os.Bundle;

import asch.io.base.activity.BaseActivity;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.util.StatusBarUtil;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/10/13.
 */

public class SplashActivity extends BaseActivity {


   // @BindView(R.id.root_ll)
   // LinearLayout rootLl;
//    @BindView(R.id.splash_pv)
//    ParticleView particleView;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        unbinder=ButterKnife.bind(this);
        StatusBarUtil.immersive(this);

        Intent intent=null;
        if (AccountsManager.getInstance().getCurrentAccount()!=null){
            intent = new Intent(SplashActivity.this, MainTabActivity.class);
        }else {
            intent = new Intent(SplashActivity.this, InitWalletActivity.class);
        }
        SplashActivity.this.startActivity(intent);
        finish();

//        particleView.startAnim();
//        particleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
//            @Override
//            public void onAnimationEnd() {
//                Intent intent=null;
//                if (AccountsManager.getInstance().getCurrentAccount()!=null){
//                     intent = new Intent(SplashActivity.this, MainTabActivity.class);
//                }else {
//                     intent = new Intent(SplashActivity.this, InitWalletActivity.class);
//                }
//                SplashActivity.this.startActivity(intent);
//                finish();
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
