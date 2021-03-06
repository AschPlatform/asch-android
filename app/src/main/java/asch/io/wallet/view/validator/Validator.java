package asch.io.wallet.view.validator;

import android.content.Context;

import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.validator.checkable.AccountNameCheckable;
import asch.io.wallet.view.validator.checkable.AddressCheckable;
import asch.io.wallet.view.validator.checkable.AmountCheckable;
import asch.io.wallet.view.validator.checkable.BCHAddressCheckable;
import asch.io.wallet.view.validator.checkable.Checkable;
import asch.io.wallet.view.validator.checkable.NickNameCheckable;
import asch.io.wallet.view.validator.checkable.NodeURLCheckable;
import asch.io.wallet.view.validator.checkable.PasswordAdvancedCheckable;
import asch.io.wallet.view.validator.checkable.PasswordCheckable;
import asch.io.wallet.view.validator.checkable.QRCodeURLCheckable;
import asch.io.wallet.view.validator.checkable.RemarkCheckable;
import asch.io.wallet.view.validator.checkable.SecondSecretCheckable;
import asch.io.wallet.view.validator.checkable.SecretCheckable;
import asch.io.wallet.view.validator.checkable.TextCheckable;

/**
 * Created by kimziv on 2017/10/31.
 */

public class Validator {

    private String TAG = this.getClass().getSimpleName();

    public enum Type{
        Secret,
        Name,
        Address,
        Amount,
        URL,
        Password,
        PasswordAdvanced,
        SecondSecret,
        QRCodeUrl,
        BchAddress,
        NickNameOrAddress,
        Remark
        ;
    }

    public static boolean check(Context context, Type type, String value, String error) {
        Checkable checkable = getTypeValidator(type,value);
        boolean valid = isValid(checkable);
        setError(context, valid, error);
        return valid;
    }

    public static boolean check(Context context, Type type, String value, String pattern, String error) {
        Checkable checkable = getTypeValidator(type,value);
        boolean valid = isValid(checkable, pattern);
        setError(context, valid, error);
        return valid;
    }

//    public static boolean check(TextInputLayout inputLayout, String error) {
//        Checkable checkable = getTypeValidator(inputLayout.getEditText());
//        boolean valid = isValid(checkable);
//        setError(inputLayout, valid, error);
//        return valid;
//    }



//    public static boolean check(TextInputLayout inputLayout, String pattern, String error) {
//        Checkable checkable = getTypeValidator(inputLayout.getEditText());
//        boolean valid = isValid(checkable, pattern);
//        setError(inputLayout, valid, error);
//        return valid;
//    }

    private static boolean isValid(Checkable checkable) {
        return checkable.check();
    }

    private static boolean isValid(Checkable checkable, String pattern) {
        return checkable.checkWithPattern(pattern);
    }

    private static void setError(Context context, boolean isValid, String error) {
        if (isValid) {
           // editText.setError(null);
        } else {
            AppUtil.toastError(context,error);
            //Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
        }
    }

//    private static void setError(TextInputLayout inputLayout, boolean isValid, String error) {
//        if (isValid) {
//            inputLayout.setErrorEnabled(false);
//            inputLayout.setError(null);
//        } else {
//            inputLayout.setErrorEnabled(true);
//            inputLayout.setError(error);
//        }
//    }

    private static Checkable getTypeValidator(Type type, String value) {

//        int type = editText.getInputType();
//        String value = editText.getText().toString();

        switch(type) {
            case Address:
                return new AddressCheckable(value);
            case  Name:
                return new AccountNameCheckable(value);
            case  URL:
                return new NodeURLCheckable(value);
            case Secret:
                return new SecretCheckable(value);
            case Password:
                return new PasswordCheckable(value);
            case Amount:
                return new AmountCheckable(value);
            case SecondSecret:
                return new SecondSecretCheckable(value);
            case QRCodeUrl:
                return new QRCodeURLCheckable(value);
            case PasswordAdvanced:
                return new PasswordAdvancedCheckable(value);
            case BchAddress:
                return new BCHAddressCheckable(value);
            case NickNameOrAddress:
                return new NickNameCheckable(value);
            case Remark:
                return new RemarkCheckable(value);
            default : return new TextCheckable(value);
        }

    }

}
