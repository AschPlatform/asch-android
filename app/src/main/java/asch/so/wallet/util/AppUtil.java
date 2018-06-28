package asch.so.wallet.util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.franmontiel.localechanger.LocaleChanger;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

import asch.so.base.util.DateConvertUtils;
import asch.so.wallet.AppConstants;
import asch.so.wallet.BuildConfig;
import asch.so.wallet.R;
import asch.so.wallet.activity.MainTabActivity;
import asch.so.wallet.model.entity.Transaction;
import es.dmoral.toasty.Toasty;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/11/17.
 */

public class AppUtil {

    public static BigDecimal decimalFromBigint(long bigInt, int precision){

        return BigDecimal.valueOf(bigInt, precision);
    }

    public  static String decimalFormat(BigDecimal decimal){
        DecimalFormat df = new DecimalFormat("#.########");
        return df.format(decimal);
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

    public static Calendar getDateByHeight(long differHeight){
        long time = System.currentTimeMillis() + differHeight*10*1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    public static void copyText(Context context, String content){
//        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
//        //ClipData clipData = ClipData.newPlainText(null, content);
//        clipboardManager.setText(content);
//        //clipboardManager.setPrimaryClip(clipData);
//        AppUtil.toastSuccess(context,"复制成功");
        copyText(context,content,context.getString(R.string.copy_success));
    }

    public static void copyText(Context context, String content, String msg){
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        //ClipData clipData = ClipData.newPlainText(null, content);
        clipboardManager.setText(content);
        //clipboardManager.setPrimaryClip(clipData);
        AppUtil.toastSuccess(context,msg);
    }

    public static void copyTextWithWarning(Context context, String content, String msg){
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(content);
        AppUtil.toastWarning(context,msg);
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
        if (BuildConfig.DEBUG || BuildConfig.TEST){
            return;
        }
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
                        if (AppConstants.SUPPORTED_LOCALES.get(2).getLanguage().equals(LocaleChanger.getLocale().getLanguage())||
                                AppConstants.SUPPORTED_LOCALES.get(2).getLanguage().equals(Locale.getDefault())){
                            if (jsonObject.containsKey("update_log_en")){
                                String updateLog= jsonObject.getString("update_log_en");
                                updateAppBean.setUpdateLog(updateLog);
                            }
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


    public static ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(100);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    public static String extractInfoFromError(Context context, Throwable ex){
        if (ex==null){
            return context.getString(R.string.error_server);
        }
        String error=ex.getMessage();
        if (error.contains("Failed to connect")){
            return context.getString(R.string.error_network);
        }else if (error.contains("Insufficient balance")){
            return context.getString(R.string.error_balance_insufficient);
        }else if (error.contains("Failed to remove vote")){
            return context.getString(R.string.error_vote_cancel);
        }else if (error.contains("Failed to add vote")){
            return context.getString(R.string.error_vote_ok);
        }else {
            return context.getString(R.string.error_params);
        }
    }

    public static void restartApp(Context context){
        Intent intent = new Intent(context, MainTabActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static int getResIdFromCode(Transaction.Type type){
        switch (type){
            case TransferV2:
            {
                return R.string.general_transfer;
            }
            case SignatureV2:
            {
                return R.string.set_second_secret;
            }
            case DelegateV2:
            {
                return R.string.register_delegate;
            }
            case VoteV2:
            {
                return R.string.vote_transaction;
            }
//            case MultiSignature:
//            {
//                return R.string.multi_signature;
//            }
//            case Dapp:
//            {
//                return R.string.dapp_transaction;
//            }
            case InTransferV2:
            {
                return R.string.in_transfer;
            }
            case OutTransferV2:
            {
                return R.string.out_transfer;
            }
//            case Store:
//            {
//                return R.string.store_transaction;
//            }
            case UIAIssuerV2:
            {
                return R.string.uia_issuer;
            }
            case UIAAssetV2:
            {
                return R.string.uia_asset;
            }
//            case UIAFlags:
//            {
//                return R.string.uia_flags;
//            }
//            case UIA_ACL:
//            {
//                return R.string.uia_acl;
//            }
            case UIAIssueV2:
            {
                return R.string.uia_issue_asset;
            }
            case UIATransferV2:
            {
                return R.string.uia_transfer;
            }
            case LockV2:
            {
                return R.string.lock_transaction;
            }
            default:
                break;

        }
        return 0;
    }

}
