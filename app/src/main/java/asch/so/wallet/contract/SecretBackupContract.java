package asch.so.wallet.contract;
import asch.so.base.presenter.BasePresenter;
import asch.so.base.view.BaseView;

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
