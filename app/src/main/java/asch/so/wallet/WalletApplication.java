package asch.so.wallet;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.activity.AppPinActivity;
import asch.so.wallet.util.IdenticonGenerator;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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

        initBuglySDK();
        AppConfig.init(this);
        Wallet.init(this);
        initAschSDK();
        //TestData.configAschSDK();
        initRealm();
        initLockManager();
        IdenticonGenerator.init(this);
        Utils.init(this);
        //CrashReport.testJavaCrash();
    }

    private void initBuglySDK() {
//        Context context = getApplicationContext();
//// 获取当前包名
//        String packageName = context.getPackageName();
//// 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
//// 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//// 初始化Bugly
//        CrashReport.initCrashReport(context, AppConstants.BUGLY_APP_ID, false, strategy);
        CrashReport.initCrashReport(getApplicationContext(), AppConstants.BUGLY_APP_ID, true);
        CrashReport.setAppChannel(this, "TEST");
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void initAschSDK() {
        String url = AppConfig.getNodeURL();
        AschSDK.Config.initBIP39(Wallet.getInstance().getMnemonicCode());
        AschSDK.Config.setAschServer(TextUtils.isEmpty(url) ? AppConstants.DEFAULT_NODE_URL : url);
        AschSDK.Config.setMagic(TestData.magic);
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        //Realm.deleteRealm(configuration);
        Realm.setDefaultConfiguration(configuration);
        // TestData.createTestAccountsData();
    }

    private void initLockManager() {
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
