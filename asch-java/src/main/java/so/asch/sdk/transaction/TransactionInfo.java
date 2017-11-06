package so.asch.sdk.transaction;

import com.alibaba.fastjson.annotation.JSONField;

import so.asch.sdk.TransactionType;
import so.asch.sdk.codec.Decoding;
import so.asch.sdk.codec.Encoding;
import so.asch.sdk.transaction.asset.AssetInfo;
import so.asch.sdk.transaction.asset.DappAssetInfo;

import java.beans.Transient;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by eagle on 17-7-16.
 */
public class TransactionInfo {
    private static final int MAX_BUFFER_SIZE = 1024 * 5;

    public Integer getType() {
        return transactionType == null ? null : transactionType.getCode();
    }

    public TransactionInfo setTransactionType(TransactionType type) {
        this.transactionType = type;
        return this;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public TransactionInfo setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
        return this;
    }

    public String getRequesterPublicKey() {
        return requesterPublicKey;
    }

    public TransactionInfo setRequesterPublicKey(String requesterPublicKey) {
        this.requesterPublicKey = requesterPublicKey;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public TransactionInfo setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getMessage() { return message; }

    public TransactionInfo setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public TransactionInfo setAmount(Long amount) {
        this.amount = amount;
        return this;
    }

    public Long getFee() {
        return fee;
    }

    public TransactionInfo setFee(Long fee) {
        this.fee = fee;
        return this;
    }

    public String getId() {
        return transactionId;
    }

    public TransactionInfo setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    @Transient
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getSignature() {
        return signature;
    }

    public TransactionInfo setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public String getSignSignature() {
        return signSignature;
    }

    public TransactionInfo setSignSignature(String signSignature) {
        this.signSignature = signSignature;
        return this;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public TransactionInfo setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @JSONField
    public AssetInfo getAsset() {
        return assetInfo;
    }

    public TransactionInfo setAsset(AssetInfo assertInfo) {
        this.assetInfo = assertInfo;
        return this;
    }

    public OptionInfo getOption() {
        return optionInfo;
    }

    public TransactionInfo setOption(OptionInfo optionInfo) {
        this.optionInfo = optionInfo;
        return this;
    }

    private String transactionId = null;
    private TransactionType transactionType = null;
    private String recipientId = null;

    private String requesterPublicKey = null;
    private String senderPublicKey = null;
    private String message = null;
    private Integer timestamp = null;
    private Long amount = null;
    private Long fee = null;

    private String signature = null;
    private String signSignature = null;
    private AssetInfo assetInfo = null;
    private OptionInfo optionInfo = null;


    public byte[] getBytes(boolean skipSignature , boolean skipSignSignature){
        //1 + 4 + 32 + 32 + 8 + 8 + 64 + 64
        //type(1)|timestamp(4)|senderPublicKey(32)|requesterPublicKey(32)|recipientId(8)|amount(8)|
        //message(?)|asset(?)|setSignature(64)|signSignature(64)

        ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        switch (transactionType){
            case Transfer:
            {
                buffer.put(getType().byteValue())
                    .putInt(getTimestamp())
                    .put(Decoding.unsafeDecodeHex(getSenderPublicKey()))
                    .put(Decoding.unsafeDecodeHex(getRequesterPublicKey()))
                    .put(getRecipientIdBuffer())
                    .putLong(getAmount())
                    .put(getMessageBuffer())
                    .put(getAsset().assetBytes());

            }
                break;
            case Signature:
                break;
            case Delegate:
                break;
            case Vote:
                break;
            case MultiSignature:
                break;
            case Dapp:
                break;
            case InTransfer:
            {
                buffer.put(getType().byteValue())
                        .putInt(getTimestamp())
                        .put(Decoding.unsafeDecodeHex(getSenderPublicKey()))
                        .put(Decoding.unsafeDecodeHex(getRequesterPublicKey()))
                        .put(getRecipientIdBuffer())
                        .putLong(getAmount())
                        .put(getMessageBuffer())
                        .put(getAsset().assetBytes());

            }
            break;
            case OutTransfer:
                break;
            case Store:
                break;
            case UIAIssuer:
                break;
            case UIAAsset:
                break;
            case UIAFlags:
                break;
            case UIA_ACL:
                break;
            case UIAIssue:
                break;
            case UIATransfer:
            {
                buffer.put(getType().byteValue())
                        .putInt(getTimestamp())
                        .put(Decoding.unsafeDecodeHex(getSenderPublicKey()))
                        .put(Decoding.unsafeDecodeHex(getRequesterPublicKey()))
                        .put(getRecipientIdBuffer())
                        .putLong(getAmount())
                        .put(getMessageBuffer())
                        .put(getAsset().assetBytes());
            }
                break;
            case Lock:
                break;
        }
        if (!skipSignature){
            buffer.put(Decoding.unsafeDecodeHex(getSignature()));
        }

        if (!skipSignSignature){
            buffer.put(Decoding.unsafeDecodeHex(getSignSignature()));
        }

        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        System.out.println("InTransfer bytes:"+ Encoding.hex(result));
        return result;
    }

    public byte[] getBytes2(boolean skipSignature , boolean skipSignSignature){

        ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE).order(ByteOrder.BIG_ENDIAN);
        switch (transactionType){
            case Transfer:
                break;
            case Signature:
                break;
            case Delegate:
                break;
            case Vote:
                break;
            case MultiSignature:
                break;
            case Dapp:
                break;
            case InTransfer:
            {
                buffer.putInt(getTimestamp())
                        .put(getDappTransactionFee())
                        .put(Decoding.unsafeDecodeHex(getSenderPublicKey()))
                        .put(getType().byteValue())
                        .put(getDappArgsBuffer());
            }
            break;
            case OutTransfer:
                break;
            case Store:
                break;
            case UIAIssuer:
                break;
            case UIAAsset:
                break;
            case UIAFlags:
                break;
            case UIA_ACL:
                break;
            case UIAIssue:
                break;
            case UIATransfer:
                break;
            case Lock:
                break;
        }
        if (!skipSignature){
            buffer.put(Decoding.unsafeDecodeHex(getSignature()));
        }

        if (!skipSignSignature){
            buffer.put(Decoding.unsafeDecodeHex(getSignSignature()));
        }

        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    private byte[] getRecipientIdBuffer(){
        if (null == recipientId)  return new byte[8];
        //数字地址
        if (recipientId.matches("^\\d+")){
            byte[] idBuffer = new BigInteger(recipientId).toByteArray();
            int length = Math.min(8 ,idBuffer.length);
            int fromIndex = idBuffer.length > 8 ? idBuffer.length - 8 : 0;
            byte[] result = new byte[8];
            System.arraycopy(idBuffer, fromIndex, result, 0, length);
            return result;
        }

        //A+Base58地址
        return Decoding.unsafeDecodeUTF8(recipientId);
    }

    private byte[] getMessageBuffer(){
        return message == null ? new byte[0] : message.getBytes();
    }

    private byte[] getDappTransactionFee(){

        return fee ==null?new byte[0]:String.valueOf(fee).getBytes();
    }

    private byte[] getDappArgsBuffer(){
        return optionInfo==null?new byte[0]:optionInfo.getArgsJson().getBytes();
    }

//    private byte[] getInTransferBuffer(DappAssetInfo assetInfo){
//        assetInfo.assetBytes();
//    }
}
