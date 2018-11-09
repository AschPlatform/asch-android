package asch.io.wallet.contract;
import asch.io.base.presenter.BasePresenter;
import asch.io.base.view.BaseView;

/**
 * Created by kimziv on 2017/11/27.
 */

public interface SecretBackupContract {
    interface View extends BaseView<Presenter> {
        void displaySecret(String secret);

    }

    interface Presenter extends BasePresenter {
        void loadSecret();

        void backupSecret(String secret);
    }
}
