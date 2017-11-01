package asch.so.wallet.view.validator.checkable;

/**
 * Created by kimziv on 2017/10/31.
 */

public class AccountNameCheckable extends BaseCheckable {

    public AccountNameCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank() && value.length()<12;
    }
}
