package asch.io.wallet.presenter.component;

import javax.inject.Singleton;

import asch.io.wallet.ApplicationModule;
import asch.io.wallet.presenter.AccountsPresenter;
import dagger.Component;

/**
 * Created by kimziv on 2017/9/21.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface PresenterComponent {
    void inject(AccountsPresenter presenter);

//    void inject(AccountImportPresenter presenter);
}
