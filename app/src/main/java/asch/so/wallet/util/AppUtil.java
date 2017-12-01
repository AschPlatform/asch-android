package asch.so.wallet.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.format.DateUtils;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.BigInteger;

import asch.so.base.util.DateConvertUtils;
import es.dmoral.toasty.Toasty;
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
    }

    public static void copyText(Context context, String content){
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        //ClipData clipData = ClipData.newPlainText(null, content);
        clipboardManager.setText(content);
        //clipboardManager.setPrimaryClip(clipData);
        AppUtil.toastSuccess(context,"复制成功");
    }

    //Custom Toast

    public static void toastError(Context context, String msg){
        Toasty.error(context,msg,Toast.LENGTH_SHORT,true).show();
    }

    public static void toastSuccess(Context context, String msg){
        Toasty.success(context,msg,Toast.LENGTH_SHORT,true).show();
    }

    public static void toastInfo(Context context, String msg){
        Toasty.info(context,msg,Toast.LENGTH_SHORT,true).show();
    }

    public static void toastWarning(Context context, String msg){
        Toasty.warning(context,msg,Toast.LENGTH_SHORT,true).show();
    }

    public static void toastNormal(Context context, String msg){
        Toasty.normal(context,msg,Toast.LENGTH_SHORT).show();
    }

}
