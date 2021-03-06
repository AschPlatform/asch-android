package asch.io.wallet;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by kimziv on 2017/9/20.
 */

public class AppConstants {

    public static final String XAS_NAME = "XAS";
    public static final int PRECISION = 8;
    public static final long XAS_CIRCULATION = 100000000;
    public static final String DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // public static final  String DEFAULT_NODE_URL="http://101.200.84.232:4097";//"http://192.168.2.124:4096";//
    public static final String DEFAULT_MAIN_NODE_URL ="http://mainnet.asch.cn"; //"http://192.168.1.78:7800";//"http://192.168.1.43:4096";//
    public static final String DEFAULT_TEST_NODE_URL ="http://testnet.asch.io";//"http://192.168.1.76:4096";//"http://47.94.37.201:4006";//http://testnet.cctime.org:4096";// "http://testnet.asch.io"; //"http://testnet.asch.so:4096";  //"http://47.93.137.170:4097";
    public static final String DEFAULT_NODE_URL = DEFAULT_MAIN_NODE_URL ;
    //public static final  String DEFAULT_NODE_URL="http://testnet.asch.io:4096";
//   public static final  String DEFAULT_MAGIC="594fe0f3";

    public static final String TESTNET_MAGIC = "594fe0f3";
    public static final String MAINNET_MAGIC = "5f5b3cf5";

//   public static final  String DEFAULT_NODE_URL="http://mainnet.asch.cn";
//   public static final  String DEFAULT_MAGIC="5f5b3cf5";

    public static final String USER_MANNUAL_URL = "http://bbs.asch.io/topic/3299/%E6%96%B0%E7%89%88%E5%AE%89%E5%8D%93%E9%92%B1%E5%8C%85%E4%BD%BF%E7%94%A8%E6%89%8B%E5%86%8C%E9%9C%87%E6%92%BC%E6%8E%A8%E5%87%BA";
    public static final String USER_MANNUAL_URL_EN = "https://bbs.asch.io/topic/3785/user-manual-of-asch-phone-wallet";

    public static final String OFFICIAL_WEBSITE_URL = "http://asch.io/";
    public static String UPDATE_URL = "http://asch-public.oss-cn-beijing.aliyuncs.com/appupdate/android/update.json";
//    public static String UPDATE_URL = "http://asch-public.oss-cn-beijing.aliyuncs.com/Android/test/update.json";

    public static final String BUGLY_APP_ID = "7e968af450";

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final int DB_SCHEME_VERSION_LAST= 2;
    public static final int DB_SCHEME_VERSION_CURRENT = 4;
    public static final int DEFAULT_CACHE_TIMEOUT = 30 * 60;//单位秒
    public static final int DEFAULT_ACCOUNT_LOCK_CACHE_TIMEOUT = 60 * 60;//单位秒

    public static final  long ONE_MONTH_SECONDS = 30 * 24 * 60 * 60;
    public static final  long ONE_MONTH_MILLS = ONE_MONTH_SECONDS * 1000;
    public static final  long ONE_MONTH_BLOCKS = ONE_MONTH_SECONDS / 10;

    public static final String DEBUG_TAG = "Debug";
    public static final String RELEASE_TAG = "Release";
    public static final int UIA_FLAG=2;
    public static final int GATEWAY_FLAG=3;

    public static  List<Locale> SUPPORTED_LOCALES =
            Arrays.asList(
                    Locale.getDefault(),
                    new Locale("zh"),
                    new Locale("en")
            );

}
