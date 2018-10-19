package asch.so.wallet.view.validator.checkable;

import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/10/31.
 */

public class NickNameCheckable extends BaseCheckable {

    public NickNameCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank()&& Validation.isValidNicknameOrAddress(value);
    }
}
