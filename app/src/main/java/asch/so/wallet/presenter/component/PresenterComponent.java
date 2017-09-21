package asch.so.wallet.presenter.component;

import javax.inject.Singleton;

import asch.so.wallet.ApplicationModule;
import asch.so.wallet.presenter.AccountsPresenter;
import dagger.Component;

/**
 * Created by kimziv on 2017/9/21.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface PresenterComponent {
    void inject(AccountsPresenter presenter);
}
