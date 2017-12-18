package asch.so.wallet;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.vector.update_app.UpdateAppManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import asch.so.base.view.Throwable;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.activity.AppPinActivity;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.util.IdenticonGenerator;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/12.
 */

public class WalletApplication extends MultiDexApplication {
    private static final String TAG = WalletApplication.class.getSimpleName();
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

    public static WalletApplication getInstance() {
        return walletApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        walletApplication = this;
        Utils.init(this);
        configLog();
        initBuglySDK();
        AppConfig.init(this);
        initWallet();
        initAschSDK();
        initRealm();
        initLockManager();
        IdenticonGenerator.init(this);
       // initLockScreenListener();
    }

    private void initWallet(){
        Wallet.init(this);
        Wallet.getInstance().loadAssets(true,new Wallet.OnLoadAssetsListener() {
            @Override
            public void onLoadAllAssets(LinkedHashMap<String, BaseAsset> assetsMap, Throwable exception) {
                LogUtils.dTag(TAG,"all assets:"+assetsMap.toString());
            }
        });
    }

    private void initBuglySDK() {
        CrashReport.initCrashReport(getApplicationContext(), AppConstants.BUGLY_APP_ID, true);
        CrashReport.setAppChannel(this, BuildConfig.LOG_DEBUG?AppConstants.DEBUG_TAG:AppConstants.RELEASE_TAG);
        String verName =AppUtils.getAppVersionName();
        int verCode= AppUtils.getAppVersionCode();
        CrashReport.setAppVersion(getApplicationContext(), String.format("%s(%d)",verName,verCode));
    }


    private void initAschSDK() {
        String url = AppConfig.getNodeURL();
        AschSDK.Config.initBIP39(Wallet.getInstance().getMnemonicCode());
        AschSDK.Config.setAschServer(TextUtils.isEmpty(url) ? AppConstants.DEFAULT_NODE_URL : url);
        AschSDK.Config.setMagic(AppConstants.DEFAULT_MAGIC);
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("wallet.db")
                .schemaVersion(AppConstants.DB_SCHEME_VERSION)
                //.deleteRealmIfMigrationNeeded()
                .build();
        //Realm.deleteRealm(configuration);
        Realm.setDefaultConfiguration(configuration);
        // TestData.createTestAccountsData();
    }

    private void initLockManager() {
        LockManager<AppPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, AppPinActivity.class);
        lockManager.getAppLock().setFingerprintAuthEnabled(true);
        lockManager.getAppLock().setOnlyBackgroundTimeout(true);
        lockManager.getAppLock().setTimeout(1000);
        lockManager.getAppLock().setLogoId(R.mipmap.ic_launcher);
        lockManager.getAppLock().setShouldShowForgot(false);
    }

    private void configLog(){
        LogUtils.getConfig().setLogSwitch(BuildConfig.LOG_DEBUG);
    }

//    private void initLockScreenListener(){
//        LockManager<AppPinActivity> lockManager = LockManager.getInstance();
//        LockScreenListener l = new LockScreenListener(this);
//        l.begin(new LockScreenListener.ScreenStateListener() {
//
//            @Override
//            public void onUserPresent() {
//                lockManager.getAppLock().setTimeout(60*60*1000);
//            }
//
//            @Override
//            public void onScreenOn() {
//                lockManager.getAppLock().
//                //Log.e("onScreenOn", "onScreenOn");
//            }
//
//            @Override
//            public void onScreenOff() {
//
//                lockManager.getAppLock().setTimeout(1000);
//            }
//        });
//    }





    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
