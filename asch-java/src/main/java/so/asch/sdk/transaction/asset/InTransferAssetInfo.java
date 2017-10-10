package so.asch.sdk.transaction.asset;

import java.nio.ByteBuffer;

import so.asch.sdk.codec.Encoding;

/**
 * Created by kimziv on 2017/10/10.
 */

public class InTransferAssetInfo extends AssetInfo {


    public  static  class  InTransferInfo{

        private String dappId;
        private long amount;
        private String currency;

        public InTransferInfo(String dappId, String currency, long amount) {
            this.dappId = dappId;
            this.amount = amount;
            this.currency = currency;
        }

        public String getDappId() {
            return dappId;
        }

        public String getAmount() {
            return Long.toString(amount);
        }

        public String getCurrency() {
            return currency;
        }

    }



    private InTransferInfo inTransfer;

    public InTransferAssetInfo(String dappId, String currency, long amount) {
        this.inTransfer = new InTransferInfo(dappId,currency,amount);
    }

    public InTransferInfo getInTransfer() {
        return inTransfer;
    }

    @Override
    public void addBytes(ByteBuffer buffer) {
        buffer.put(Encoding.getUTF8Bytes(inTransfer.getDappId()));
        buffer.put(Encoding.getUTF8Bytes(inTransfer.getCurrency()));
        if (!inTransfer.getCurrency().equals("XAS")){
            buffer.put(Encoding.getUTF8Bytes(inTransfer.getAmount()));
        }
    }
}
