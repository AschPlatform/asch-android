package asch.so.wallet.view.validator.checkable;

import android.widget.EditText;

/**
 * Created by kimziv on 2017/11/1.
 */

public class AmountCheckable extends BaseCheckable {
    public AmountCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
      if (!isNotBlank()){
         return false;
      }
      try {
          float amount=  Float.parseFloat(value);
          if (amount>0){
              return true;
          }
      }catch (Exception e){
          return false;
      }
      return false;
    }
}
