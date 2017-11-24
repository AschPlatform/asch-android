package asch.so.wallet.view.validator;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import asch.so.wallet.view.validator.checkable.AccountNameCheckable;
import asch.so.wallet.view.validator.checkable.AddressCheckable;
import asch.so.wallet.view.validator.checkable.AmountCheckable;
import asch.so.wallet.view.validator.checkable.Checkable;
import asch.so.wallet.view.validator.checkable.NodeURLCheckable;
import asch.so.wallet.view.validator.checkable.PasswordCheckable;
import asch.so.wallet.view.validator.checkable.QRCodeURLCheckable;
import asch.so.wallet.view.validator.checkable.SecondSecretCheckable;
import asch.so.wallet.view.validator.checkable.SecretCheckable;
import asch.so.wallet.view.validator.checkable.TextCheckable;

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
        SecondSecret,
        QRCodeUrl,
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
           // editText.setError(error);
            Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
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
            default : return new TextCheckable(value);
        }

    }

}
