package asch.so.wallet;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kimziv on 2017/9/20.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    WalletApplication getApplication();

    Context getContext();
}
