package asch.so.wallet;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by kimziv on 2017/9/12.
 */

public class WalletApplication extends MultiDexApplication{
    private  static final String TAG = WalletApplication.class.getSimpleName();
    private ApplicationComponent applicationComponent;
    private static WalletApplication walletApplication;


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
        initRealm();
    }

    private void initRealm(){
        Realm.init(this);
        RealmConfiguration configuration=new RealmConfiguration.Builder().build();
        //Realm.deleteRealm(configuration);
        Realm.setDefaultConfiguration(configuration);
        TestData.createTestAccountsData();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
