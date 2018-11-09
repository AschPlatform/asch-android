package asch.io.wallet.view.validator.checkable;

/**
 * Created by kimziv on 2017/10/31.
 */

public interface Checkable {
    boolean isNotBlank();

    boolean check();

    boolean checkWithPattern(String pattern);
}
