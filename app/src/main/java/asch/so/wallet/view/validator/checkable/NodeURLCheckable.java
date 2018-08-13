package asch.so.wallet.view.validator.checkable;

import com.blankj.utilcode.util.RegexUtils;
//import com.litesuits.common.assist.Check;

/**
 * Created by kimziv on 2017/11/1.
 */

public class NodeURLCheckable extends BaseCheckable {

    public NodeURLCheckable(String value) {
        super(value);
    }

    @Override
    public boolean check() {
        return isNotBlank() && RegexUtils.isURL(value);
    }
}
