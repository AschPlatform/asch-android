package asch.so.wallet.view.validator.checkable;

/**
 * Created by kimziv on 2017/10/31.
 */

public class AddressCheckable extends BaseCheckable {

    public AddressCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return value.startsWith("A");
    }
}
