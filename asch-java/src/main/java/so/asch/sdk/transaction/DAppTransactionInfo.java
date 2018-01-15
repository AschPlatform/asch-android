package so.asch.sdk.transaction;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import so.asch.sdk.ContractType;
import so.asch.sdk.TransactionType;
import so.asch.sdk.codec.Decoding;
import so.asch.sdk.codec.Encoding;
import so.asch.sdk.transaction.asset.AssetInfo;

/**
 * Created by kimziv on 2018/1/15.
 */

public class DAppTransactionInfo {
    private static final int MAX_BUFFER_SIZE = 1024 * 5;

    public String getFee() {
        return fee;
    }

    public DAppTransactionInfo setFee(String fee) {
        this.fee = fee;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public DAppTransactionInfo setType(Integer type) {
        this.type = type;
        return this;
    }

//    public DAppTransactionInfo setContractType(ContractType contractType) {
//        this.contractType = contractType;
//        return this;
//    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public DAppTransactionInfo setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
        return this;
    }

    public String getRequesterPublicKey() {
        return requesterPublicKey;
    }

    public DAppTransactionInfo setRequesterPublicKey(String requesterPublicKey) {
        this.requesterPublicKey = requesterPublicKey;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public DAppTransactionInfo setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getMessage() { return message; }

    public DAppTransactionInfo setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public DAppTransactionInfo setAmount(Long amount) {
        this.amount = amount;
        return this;
    }

    public String getId() {
        return transactionId;
    }

    public DAppTransactionInfo setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public String getSignature() {
        return signature;
    }

    public DAppTransactionInfo setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public String getSignSignature() {
        return signSignature;
    }

    public DAppTransactionInfo setSignSignature(String signSignature) {
        this.signSignature = signSignature;
        return this;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public DAppTransactionInfo setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @JSONField
    public AssetInfo getAsset() {
        return assetInfo;
    }

    public DAppTransactionInfo setAsset(AssetInfo assertInfo) {
        this.assetInfo = assertInfo;
        return this;
    }

    public OptionInfo getOption() {
        return optionInfo;
    }

    public DAppTransactionInfo setOption(OptionInfo optionInfo) {
        this.optionInfo = optionInfo;
        return this;
    }

    public String getArgs() {
        return args;
    }

    public DAppTransactionInfo setArgs(String args) {
        this.args = args;
        return this;
    }

    private String transactionId = null;
    private transient ContractType contractType = null;
    private String recipientId = null;

    private String requesterPublicKey = null;
    private String senderPublicKey = null;
    private String message = null;
    private Integer timestamp = null;
    private Long amount = null;
    private String fee = null;
    private Integer type = null;
    private String args=null;

    private String signature = null;
    private String signSignature = null;
    private AssetInfo assetInfo = null;
    private OptionInfo optionInfo = null;



    public byte[] getBytes(boolean skipSignature , boolean skipSignSignature){
        //1 + 4 + 32 + 32 + 8 + 8 + 64 + 64
        //type(1)|timestamp(4)|senderPublicKey(32)|requesterPublicKey(32)|recipientId(8)|amount(8)|
        //message(?)|asset(?)|setSignature(64)|signSignature(64)

        ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN);
       // if (optionInfo==null){
            buffer=getDappBytes(buffer);
       // }

        if (!skipSignature){
            buffer.put(Decoding.unsafeDecodeHex(getSignature()));
        }

        if (!skipSignSignature){
            buffer.put(Decoding.unsafeDecodeHex(getSignSignature()));
        }

        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        System.out.println("TransactionInfo bytes:"+ Encoding.hex(result));
        return result;
    }


    public ByteBuffer getDappBytes(ByteBuffer buffer){
        //1 + 4 + 32 + 32 + 8 + 8 + 64 + 64
        //type(1)|timestamp(4)|senderPublicKey(32)|requesterPublicKey(32)|recipientId(8)|amount(8)|
        //message(?)|asset(?)|setSignature(64)|signSignature(64)

        switch (optionInfo.getType()){
            case CoreDeposit:
                break;
            case CoreWithdrawal:
            {

                buffer.putInt(getTimestamp())
                        .put(getDappTransactionFee())
                        .put(Decoding.unsafeDecodeHex(getSenderPublicKey()))
                        .put(getDappTransactionType().byteValue())
                        .put(getDappArgsBuffer());
            }
            break;
            case CoreTransfer:
                break;
            case CoreSetNickname:
                break;


            case CCTimePostArticle:
                break;
            case CCTimePostComment:
                break;
            case CCTimeVoteArticle:
                break;
            case CCTimeLikeComment:
                break;
            case CCTimeReport:
                break;
        }
        return buffer;
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

        return optionInfo ==null?new byte[0]:String.valueOf(optionInfo.getFee()).getBytes();
    }

    private Integer getDappTransactionType(){
        return  optionInfo==null?null:optionInfo.getType().getCode();
    }

    private byte[] getDappArgsBuffer(){
        return optionInfo==null?new byte[0]:optionInfo.getArgsJson().getBytes();
    }

}
