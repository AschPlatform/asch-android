package asch.so.wallet.view.validator.checkable;

import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/10/31.
 */

public class AddressCheckable extends BaseCheckable {

    public AddressCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank()&& Validation.isValidAddress(value);
    }
}
