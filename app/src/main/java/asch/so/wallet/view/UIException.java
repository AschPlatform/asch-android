package asch.so.wallet.view;

/**
 * Created by kimziv on 2017/11/6.
 */

public class UIException extends Exception {
    private static final long serialVersionUID = 1L;

    public UIException(String message, Throwable parent) {
        super(message, parent);
    }

    public UIException(String message) {
        super(message);
    }

    public UIException(Throwable t) {
        super(t);
    }
}
