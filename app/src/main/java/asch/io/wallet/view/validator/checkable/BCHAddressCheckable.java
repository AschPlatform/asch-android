package asch.io.wallet.view.validator.checkable;

import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/10/31.
 */

public class BCHAddressCheckable extends BaseCheckable {

    public BCHAddressCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank()&& Validation.isValidBCHAddress(value);
    }
}
