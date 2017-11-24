package asch.so.wallet.view.validator.checkable;

import java.io.UnsupportedEncodingException;

import asch.so.wallet.model.entity.QRCodeURL;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/11/24.
 */

public class QRCodeURLCheckable extends BaseCheckable {
    public QRCodeURLCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        QRCodeURL url=null;
        try {
             url= QRCodeURL.decodeQRCodeURL(value);
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
            return  false;
        }
        return (url!=null) && Validation.isValidAddress(url.getAddress());
    }
}
