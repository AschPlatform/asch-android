package asch.so.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//import junit.framework.Assert;

import so.asch.sdk.AschHelper;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

public class MainActivity extends AppCompatActivity {

    public static final String root = "http://testnet.asch.so:4096";
    public static final String secret = "spatial slush action typical emerge feature confirm edge game desk orphan burst";
    public static final String address = "AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym";
//    public static final String secondSecret = "asch111111";
//    public static final String userName = "asch_g2";

    private  TextView mTv;
    private static final String mTAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    // Example of a call to a native method
    mTv = (TextView) findViewById(R.id.sample_text);
        mTv.setText(stringFromJNI());
        new Thread(){
            @Override
            public void run() {
                testSDK();
            }
        }.start();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private  void testSDK(){
        AschSDK.Config.setAschServer(root);
        AschResult result= AschSDK.Account.login(secret);
        String rawJson=result.getRawJson();
        Log.d("++++++++"+MainActivity.class.getSimpleName(), rawJson+" ");
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mTv.setText(result.getRawJson());
//            }
//        });

        //Assert.assertTrue(result.isSuccessful());

        result= AschSDK.Account.secureLogin(TestData.secret);
        Log.d("++++++++"+MainActivity.class.getSimpleName()+"-secureLogin:", rawJson+" ");
        //Assert.assertTrue(result.isSuccessful());

        //AschHelper helper =new AschHelper();
        //String secret= helper.generateSecret();
       // Log.d(mTAG,"++++++++ "+secret);

    }
}
