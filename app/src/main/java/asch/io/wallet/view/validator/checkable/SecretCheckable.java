package asch.io.wallet.view.validator.checkable;

import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/11/1.
 */

public class SecretCheckable extends BaseCheckable {

    public SecretCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return Validation.isValidSecret(value);
       //return checkWithPattern("([a-z]+[\\s]+){11}[a-z]+");
    }
}
