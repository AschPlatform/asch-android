package asch.io.wallet.activity.component;

import asch.io.wallet.ApplicationComponent;
import asch.io.wallet.activity.AccountsActivity;
import asch.io.wallet.activity.module.AccountsModule;
import asch.io.wallet.util.ActivityScoped;
import dagger.Component;

/**
 * Created by kimziv on 2017/9/21.
 */

@ActivityScoped
@Component(modules = AccountsModule.class, dependencies = ApplicationComponent.class)
public interface AccountsManagerComponent {

    void inject(AccountsActivity accountsActivity);
}
