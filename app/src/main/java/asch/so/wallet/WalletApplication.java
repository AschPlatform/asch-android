package asch.so.wallet;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import asch.so.wallet.activity.AppPinActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by kimziv on 2017/9/12.
 */

public class WalletApplication extends MultiDexApplication{
    private  static final String TAG = WalletApplication.class.getSimpleName();
    private ApplicationComponent applicationComponent;
    private static WalletApplication walletApplication;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    public static WalletApplication getInstance(){
        return walletApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent=DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        walletApplication=this;

        AppConfig.init(this);
        initRealm();
        initLockManager();
    }

    private void initRealm(){
        Realm.init(this);
        RealmConfiguration configuration=new RealmConfiguration.Builder().build();
        //Realm.deleteRealm(configuration);
        Realm.setDefaultConfiguration(configuration);
       // TestData.createTestAccountsData();
        TestData.configAschSDK();
        //TestData.testSDK();
       // TestData.testED25519();
    }

    private void initLockManager(){
        LockManager<AppPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, AppPinActivity.class);
        lockManager.getAppLock().setFingerprintAuthEnabled(true);
        lockManager.getAppLock().setOnlyBackgroundTimeout(true);
        lockManager.getAppLock().setTimeout(5000);
        lockManager.getAppLock().setLogoId(R.mipmap.ic_launcher);
        lockManager.getAppLock().setShouldShowForgot(false);
       // lockManager.getAppLock().setPinChallengeCancelled(true);
    }


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
