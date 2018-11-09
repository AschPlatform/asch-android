package asch.io.wallet.view.validator.checkable;

/**
 * Created by kimziv on 2017/11/1.
 */

public class PasswordCheckable extends BaseCheckable {
    public PasswordCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank() && value.length()>=8;
    }
}
