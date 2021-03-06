package asch.io.wallet.view.validator.checkable;

/**
 * Created by kimziv on 2017/10/31.
 */

public class AccountNameCheckable extends BaseCheckable {

    public AccountNameCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank() && checkWithPattern("[A-Za-z0-9_]{6,12}");
//        return isNotBlank() && checkWithPattern("[A-Za-z0-9_]{1,12}");
    }
}
