package asch.io.wallet.view.validator.checkable;

/**
 * Created by kimziv on 2017/11/15.
 */

public class SecondSecretCheckable extends BaseCheckable {

    public SecondSecretCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        //8-16位字符串验证，必须同时包含字母和数字
        return isNotBlank()&& checkWithPattern("^(?=.*[0-9])(?=.*[a-zA-Z])(.{8,16})$");// checkWithPattern("[A-Za-z0-9]{8,16}");
    }
}
