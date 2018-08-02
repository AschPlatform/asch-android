package asch.so.wallet;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.franmontiel.localechanger.LocaleChanger;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
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
import java.net.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

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

        LocaleChanger.initialize(getApplicationContext(), AppConstants.SUPPORTED_LOCALES);

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
        setupDownloader();
       // initLockScreenListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
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
        url=TextUtils.isEmpty(url) ? AppConstants.DEFAULT_NODE_URL : url;
        AschSDK.Config.initBIP39(Wallet.getInstance().getMnemonicCode());
        AschSDK.Config.setAschServer(url);
        AschSDK.Config.setLongTransactionIdEnabled(true);
        if (url.contains("mainnet")){
            AschSDK.Config.setMagic(AppConstants.MAINNET_MAGIC);
        }else {
            AschSDK.Config.setMagic(AppConstants.TESTNET_MAGIC);
        }

    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("wallet.db")
                .schemaVersion(AppConstants.DB_SCHEME_VERSION_CURRENT)
                .migration(new DBMigration())
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


    private void setupDownloader(){
        FileDownloadLog.NEED_LOG = BuildConfig.LOG_DEBUG;
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                ))
                .commit();

//        // below codes just for monitoring thread pools in the FileDownloader:
//        IThreadDebugger debugger = ThreadDebugger.install(
//                ThreadDebuggers.create() /** The ThreadDebugger with known thread Categories **/
//                        // add Thread Category
//                        .add("OkHttp").add("okio").add("Binder")
//                        .add(FileDownloadUtils.getThreadPoolName("Network"), "Network")
//                        .add(FileDownloadUtils.getThreadPoolName("Flow"), "FlowSingle")
//                        .add(FileDownloadUtils.getThreadPoolName("EventPool"), "Event")
//                        .add(FileDownloadUtils.getThreadPoolName("LauncherTask"), "LauncherTask")
//                        .add(FileDownloadUtils.getThreadPoolName("ConnectionBlock"), "Connection")
//                        .add(FileDownloadUtils.getThreadPoolName("RemitHandoverToDB"), "RemitHandoverToDB")
//                        .add(FileDownloadUtils.getThreadPoolName("BlockCompleted"), "BlockCompleted"),
//
//                2000, /** The frequent of Updating Thread Activity information **/
//
//                new ThreadDebugger.ThreadChangedCallback() {
//                    /**
//                     * The threads changed callback
//                     **/
//                    @Override
//                    public void onChanged(IThreadDebugger debugger) {
//                        // callback this method when the threads in this application has changed.
//                        Log.d(TAG, debugger.drawUpEachThreadInfoDiff());
//                        Log.d(TAG, debugger.drawUpEachThreadSizeDiff());
//                        Log.d(TAG, debugger.drawUpEachThreadSize());
//                    }
//                });
    }

    private void configLog(){
        LogUtils.getConfig().setLogSwitch(BuildConfig.LOG_DEBUG);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
