package so.asch.sdk.transaction.asset;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.PropertyFilter;

import java.nio.ByteBuffer;

import so.asch.sdk.codec.Encoding;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/10/10.
 */

public class InTransferAssetInfo extends AssetInfo {


    public  static  class  InTransferInfoBase{

        protected String dappId;
        protected String currency;

        public InTransferInfoBase(String dappId, String currency) {
            this.dappId = dappId;
            this.currency = currency;
        }

        public String getDappId() {
            return dappId;
        }

        public String getCurrency() {
            return currency;
        }

    }

    public  static  class  InTransferInfo extends InTransferInfoBase{

        private String amount;

        public InTransferInfo(String dappId, String currency, long amount) {
            super(dappId,currency);
            this.amount = String.valueOf(amount);
        }

        public String getAmount() {
            return amount;
        }

    }

    private InTransferInfoBase inTransfer;

    public InTransferAssetInfo(String dappId, String currency, long amount) {
        if (AschConst.CORE_COIN_NAME.equals(currency))
        {
            this.inTransfer = new InTransferInfoBase(dappId,currency);
        }
        else{
            this.inTransfer = new InTransferInfo(dappId,currency,amount);
        }
    }

//    public InTransferAssetInfo(String dappId, String currency) {
//        this.inTransfer = new InTransferInfoBase(dappId,currency);
//    }

    public InTransferInfoBase getInTransfer() {
        return inTransfer;
    }

    @Override
    public void addBytes(ByteBuffer buffer) {
        buffer.put(Encoding.getUTF8Bytes(inTransfer.getDappId()));
        buffer.put(Encoding.getUTF8Bytes(inTransfer.getCurrency()));
        if (!inTransfer.getCurrency().equals("XAS")){
            buffer.put(Encoding.getUTF8Bytes(((InTransferInfo)inTransfer).getAmount()));
        }
    }
}
