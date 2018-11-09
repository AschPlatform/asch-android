package asch.io.wallet.view.validator.checkable;

import java.util.regex.Pattern;

/**
 * Created by kimziv on 2017/10/31.
 */

public abstract class BaseCheckable implements Checkable {
    protected String value;

    public BaseCheckable(String value) {
        this.value = value;
    }

    @Override
    public boolean isNotBlank() {
        return value != null && !value.isEmpty() && !value.contains(" ");
    }

    abstract public boolean check();

    @Override
    public boolean checkWithPattern(String pattern) {
        return Pattern.matches(pattern, value);
    }
}
