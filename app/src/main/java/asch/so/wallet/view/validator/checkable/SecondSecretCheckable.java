package asch.so.wallet.view.validator.checkable;

/**
 * Created by kimziv on 2017/11/15.
 */

public class SecondSecretCheckable extends BaseCheckable {

    public SecondSecretCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank();
    }
}
