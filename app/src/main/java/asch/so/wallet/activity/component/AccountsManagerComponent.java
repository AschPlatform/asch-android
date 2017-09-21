package asch.so.wallet.activity.component;

import asch.so.wallet.ApplicationComponent;
import asch.so.wallet.activity.AccountsActivity;
import asch.so.wallet.activity.module.AccountsModule;
import asch.so.wallet.util.ActivityScoped;
import dagger.Component;

/**
 * Created by kimziv on 2017/9/21.
 */

@ActivityScoped
@Component(modules = AccountsModule.class, dependencies = ApplicationComponent.class)
public interface AccountsManagerComponent {

    void inject(AccountsActivity accountsActivity);
}
