package asch.so.wallet.view.validator.checkable;

/**
 * Created by kimziv on 2017/10/31.
 */

public class TextCheckable extends BaseCheckable {

    public TextCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank();
    }
}
