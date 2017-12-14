package asch.so.wallet.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.math.BigDecimal;
import java.math.BigInteger;

import asch.so.base.util.DateConvertUtils;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import es.dmoral.toasty.Toasty;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/11/17.
 */

public class AppUtil {

    public static BigDecimal decimalFromBigint(long bigInt, int precision){

        return BigDecimal.valueOf(bigInt, precision);
    }

    public static long scaledAmountFromDecimal(float amount, int precision){
        long scaled_precision = 1;
        for (int i = 0; i < precision; ++i) {
            scaled_precision *= 10;
        }
        return (long) (amount*scaled_precision);
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
        Toast toast = Toasty.error(context,msg,Toast.LENGTH_SHORT,true);
        toast.setGravity(Gravity.CENTER, 0, ConvertUtils.dp2px(-130));
        toast.show();

    }

    public static void toastSuccess(Context context, String msg){
        Toast toast = Toasty.success(context,msg,Toast.LENGTH_SHORT,true);
        toast.setGravity(Gravity.CENTER, 0, ConvertUtils.dp2px(-130));
        toast.show();
    }

    public static void toastInfo(Context context, String msg){
        Toast toast = Toasty.info(context,msg,Toast.LENGTH_SHORT,true);
        toast.setGravity(Gravity.CENTER, 0, ConvertUtils.dp2px(-130));
        toast.show();
    }

    public static void toastWarning(Context context, String msg){
        Toast toast = Toasty.warning(context,msg,Toast.LENGTH_SHORT,true);
        toast.setGravity(Gravity.CENTER, 0, ConvertUtils.dp2px(-130));
        toast.show();
    }

    public static void toastNormal(Context context, String msg){
        Toast toast = Toasty.normal(context,msg,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, ConvertUtils.dp2px(-130));
        toast.show();
    }


    public static void updateApp(Activity activity) {
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(activity)
                //更新地址
                .setUpdateUrl(AppConstants.UPADATE_URL)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .setTopPic(R.mipmap.update_top)
                .setThemeColor(0xff32b3e3)
                .build()
                .checkNewApp(new UpdateCallback(){
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean= JSON.parseObject(json,UpdateAppBean.class);
                        JSONObject jsonObject = JSON.parseObject(json);

                        int versionCode = jsonObject.getIntValue("version_code");
                        int currentVersionCode= AppUtils.getAppVersionCode();
                        if (versionCode<=currentVersionCode){
                            updateAppBean.setUpdate("No");
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        super.hasNewApp(updateApp, updateAppManager);
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                    }

                    @Override
                    protected void noNewApp() {
                        super.noNewApp();
                    }

                    @Override
                    protected void onBefore() {
                        super.onBefore();
                    }
                });
    }

}
