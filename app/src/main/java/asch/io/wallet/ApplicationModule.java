package asch.io.wallet;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kimziv on 2017/9/20.
 */
@Module
public class ApplicationModule {
    private Context context;

    public ApplicationModule(Context ctx){
        this.context=ctx;
    }

    @Provides
    @Singleton
    WalletApplication provideApplication(){

        return (WalletApplication) context.getApplicationContext();
    }

    @Provides
    @Singleton
    Context provideContext(){
        return context;
    }

}
