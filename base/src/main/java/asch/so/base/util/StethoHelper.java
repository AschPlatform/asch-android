package asch.so.base.util;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * @author kimziv (kimziv@qq.com)
 *         2017/7/25
 */
public interface StethoHelper {

    void init(Context context);

    OkHttpClient.Builder addNetworkInterceptor(OkHttpClient.Builder builder);
}
