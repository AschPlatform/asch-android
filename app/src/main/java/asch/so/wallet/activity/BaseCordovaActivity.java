package asch.so.wallet.activity;

import android.os.Bundle;
import android.text.TextUtils;

import org.apache.cordova.CordovaActivity;

/**
 * Created by kimziv on 2017/12/29.
 */

public class BaseCordovaActivity extends CordovaActivity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }
        String launchUrl=extras.getString("url");
         //launchUrl = TextUtils.isEmpty(launchUrl)? "file:///android_asset/www/index.html":"file://"+launchUrl;
        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);
    }
}
