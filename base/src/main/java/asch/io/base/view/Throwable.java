package asch.io.base.view;

/**
 * Created by kimziv on 2017/11/6.
 */

public class Throwable extends Exception {
    private static final long serialVersionUID = 1L;

    public Throwable(String message, java.lang.Throwable parent) {
        super(message, parent);
    }

    public Throwable(String message) {
        super(message);
    }

    public Throwable(java.lang.Throwable t) {
        super(t);
    }
}
