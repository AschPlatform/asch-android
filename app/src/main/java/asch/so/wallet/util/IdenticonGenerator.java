package asch.so.wallet.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.litesuits.common.io.FileUtils;

import asch.so.wallet.R;

/**
 * Created by kimziv on 2017/11/9.
 */

public class IdenticonGenerator {
    private static final String TAG = IdenticonGenerator.class.getSimpleName();
    private static IdenticonGenerator instance;

    private static Context context;

    private static BridgeWebView webView;

    public static void init(Context ctx) {
        context = ctx;
        webView = new BridgeWebView(context);
        webView.setDefaultHandler(new DefaultHandler());
        webView.loadUrl("file:///android_asset/jdenticontest.html");
    }

    public static IdenticonGenerator getInstance() {
        if (instance == null) {
            instance = new IdenticonGenerator();
        }
        return instance;
    }

    public void generateBitmap(String value, OnIdenticonGeneratorListener listener) {
//       Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_avatar);
//        if (listener != null) {
//            listener.onIdenticonGenerated(bmp);
//        }
//        return;
        Bitmap bitmap = value == null ? null : CacheUtils.getInstance().getBitmap(value);
        LogUtils.dTag(TAG, "bitmap info:" + (bitmap != null ? bitmap.toString() : "null"));
        if (bitmap != null) {
            if (listener != null) {
                listener.onIdenticonGenerated(bitmap);
            }
            return;
        }
        webView.callHandler("functionInJs", value, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                final String pureBase64Encoded = data.substring(data.indexOf(",") + 1);
                byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (listener != null) {
                    listener.onIdenticonGenerated(bmp);
                }
                CacheUtils.getInstance().put(value, bmp);
            }
        });
    }

    public interface OnIdenticonGeneratorListener {
        void onIdenticonGenerated(Bitmap bmp);
    }
}
