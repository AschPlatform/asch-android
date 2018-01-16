package so.asch.sdk.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by eagle on 17-7-18.
 */
public final class AschConst {
    public static final int CLIENT_DRIFT_SECONDS = 5;
    public static final int COIN = 100000000;
    public static final char BASE58_ADDRESS_PREFIX = 'A';
    public static final int BASE58_ADDRESS_LEN = 34 ;
    public static final Date ASCH_BEGIN_EPOCH;
    public static final String CORE_COIN_NAME="XAS";
    public static final int CORE_COIN_PRECISION=8;
    //public static final long CORE_COIN_QU

    static {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse("2016-06-27T20:00:00Z");
        }
        catch (Exception ex){
            //never
        }
        ASCH_BEGIN_EPOCH = date;
    }

    public static class Fees{
        public static final long TRANSFER = 10000000;
        public static final long UIA_TRANSFER = 10000000;
        public static final long VOTE = 10000000;
        public static final long SECOND_SIGNATURE =500000000;
        public static final long MULTI_SIGNATURE =500000000;
        public static final long DELEGATE = 10000000000L;
        public static final long DAPP = 10000000000L;
        public static final long DAPP_WITHDRAW = 10000000L;
        public static final long DAPP_TRANSFER = 10000000L;
        public static final long DAPP_SET_NICKNAME = 10000000L;
    }



    public class AccountFields{
        public static final String NAME = "name";

    }
}
