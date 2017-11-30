package asch.so.wallet.util;

import android.content.Context;
import android.text.format.DateUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import asch.so.base.util.DateConvertUtils;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/11/17.
 */

public class AppUtil {

    public static BigDecimal decimalFromBigint(long bigInt, int precision){

        return BigDecimal.valueOf(bigInt, precision);
    }

    public static CharSequence getRelativeTimeSpanString(Context context, long millis){
        int flags = DateUtils.FORMAT_SHOW_DATE
                |DateUtils.FORMAT_SHOW_YEAR
                |DateUtils.FORMAT_ABBREV_MONTH
                ;
        return DateUtils.getRelativeTimeSpanString(millis,System.currentTimeMillis(),DateUtils.MINUTE_IN_MILLIS,flags);

//        return DateUtils.getRelativeDateTimeString(context, millis,DateUtils.MINUTE_IN_MILLIS,DateUtils.MINUTE_IN_MILLIS,flags);
    }

}
