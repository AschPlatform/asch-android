package asch.so.wallet.view.validator.checkable;


import java.util.regex.Pattern;

public class PasswordAdvancedCheckable extends BaseCheckable {
    public PasswordAdvancedCheckable(String value) {
        super(value);
    }

    /**
     * 8-16位，同时包含字母、数字、符号
     * @return
     */
    @Override
    public boolean check() {

        Boolean isMatchA_Z = Pattern.matches(".*[a-zA-z].*", value);
        Boolean isMatch0_9 = Pattern.matches(".*[0-9].*", value);
        Boolean is8_16 = Pattern.matches("^.{8,16}$", value);
        //剔除字母与数字后，是否还剩字符
        String result = value.replaceAll("[0-9a-zA-Z]","");
        Boolean isContainOtherSymbol = !result.isEmpty();
        Boolean isValid =  isMatchA_Z && isMatch0_9 && is8_16 && isContainOtherSymbol;

        return isNotBlank() && isValid;

    }
}
