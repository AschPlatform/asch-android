package so.asch.sdk.transaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import so.asch.sdk.ContractType;
import so.asch.sdk.Transaction;
import so.asch.sdk.TransactionType;
import so.asch.sdk.codec.Decoding;
import so.asch.sdk.codec.Encoding;
import so.asch.sdk.impl.FeeCalculater;
import so.asch.sdk.transaction.asset.AssetInfo;
import so.asch.sdk.transaction.asset.DappAssetInfo;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by eagle on 17-7-16.
 */
public class TransactionInfo {
    private static final int MAX_BUFFER_SIZE = 1024 * 5;



    @JSONField(name = "id")
    private String transactionId = null;
    private transient TransactionType transactionType = null;
    private String recipientId = null;

    private String requestorId = null;
    private String senderId = null;

    private String requesterPublicKey = null;
    private String senderPublicKey = null;
    private String message = null;
    private Integer timestamp = null;
    private Long amount = null;
    private Long fee = null;
    private ContractType contractType=null;
    //private String signature = null;
    private String[] signatures = null;
    //    private String signSignature = null;
    private String secondSignature=null;
    private AssetInfo assetInfo = null;
    private transient OptionInfo optionInfo = null;
    //private String args=null;
    private Object[] args = null;


    public Integer getType() {
        return transactionType == null ? null : transactionType.getCode();
    }



    public TransactionInfo setTransactionType(TransactionType type) {
        this.transactionType = type;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public TransactionInfo setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public TransactionInfo setRequestorId(String requestorId) {
        this.requestorId = requestorId;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String[] getSignatures() {
        return signatures;
    }

    public TransactionInfo setSignatures(String[] signatures) {
        this.signatures = signatures;
        return this;
    }

    public TransactionInfo setSignature(String signature) {
        this.setSignatures( new String[]{ signature } );
        return this;
    }

//    public String getSignature() {
//        return signature;
//    }
//
//    public TransactionInfo setSignature(String signature) {
//        this.signature = signature;
//        return this;
//    }

//    public String getSignSignature() {
//        return signSignature;
//    }
//
//    public TransactionInfo setSignSignature(String signSignature) {
//        this.signSignature = signSignature;
//        return this;
//    }


    public String getSecondSignature() {
        return secondSignature;
    }

    public void setSecondSignature(String secondSignature) {
        this.secondSignature = secondSignature;
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

    @JSONField
    public Object[] getArgs() {
        return args;
    }

    public TransactionInfo setArgs(Object[] args) {
        this.args = args;
        return this;
    }

//    public String getArgs() {
//        return args;
//    }
//
//    public TransactionInfo setArgs(String args) {
//        this.args = args;
//        return this;
//    }

    public ContractType getContractType() {
        return contractType;
    }

    public TransactionInfo setContractType(ContractType contractType) {
        this.contractType = contractType;
        return this;
    }



    public byte[] getBytes(boolean skipSignature , boolean skipSignSignature){
        // 4 + 4 + 8 + 32 + 32 + ? + ? + 32 + 32
        // type(4)|timestamp(4)|fee(8)|senderId(32)|[requestorId(32)]|[message(?)]|
        // args(?)|signature(32)|[signSignature(32)]

        ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
                .putInt(getType())
                .putInt(getTimestamp())
                .putLong(getFee())
                .put(getSenderIdBuffer())
                //.put(getRequestorIdBuffer())
                .put(getMessageBuffer())
                .put(getArgsBuffer());

        if (!skipSignature && this.getSignatures()!=null){
           // buffer.put(Decoding.unsafeDecodeHex(getSignature()));
            buffer.put(getSignaturesBuffer());
        }

        if (!skipSignSignature && this.getSecondSignature()!=null){
            buffer.put(Decoding.unsafeDecodeHex(getSecondSignature()));
        }

        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);

        return result;
    }

    public byte[] getBytes0(boolean skipSignature , boolean skipSignSignature){
        //1 + 4 + 32 + 32 + 8 + 8 + 64 + 64
        //type(1)|timestamp(4)|senderPublicKey(32)|requesterPublicKey(32)|recipientId(8)|amount(8)|
        //message(?)|asset(?)|setSignature(64)|signSignature(64)
        //------------------------------------------------
        //new layout
        // 4 + 4 + 8 + 32 + 32 + ? + ? + 32 + 32
        // type(4)|timestamp(4)|fee(8)|senderId(32)|[requestorId(32)]|[message(?)]|
        // args(?)|signature(32)|[signSignature(32)]

        ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        if (optionInfo==null||optionInfo.getType()==ContractType.None){
            switch (transactionType){
                case basic_transfer:
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
                case basic_setPassword:
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
//                case Delegate:
//                    break;
                case Vote:
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
                {
                    buffer.put(getType().byteValue())
                            .putInt(getTimestamp())
                            .put(Decoding.unsafeDecodeHex(getSenderPublicKey()))
                            .put(Decoding.unsafeDecodeHex(getRequesterPublicKey()))
                            .put(getRecipientIdBuffer())
                            .putLong(getAmount())
                            .put(getArgsBuffer());

                }
                    break;
            }
        }else {
          buffer=getDappBytes(buffer);
        }

        if (!skipSignature){
            //buffer.put(Decoding.unsafeDecodeHex(getSignature()));
            buffer.put(getSignaturesBuffer());
        }

        if (!skipSignSignature){
            buffer.put(Decoding.unsafeDecodeHex(getSecondSignature()));
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
        buffer.putInt(getTimestamp())
                .put(getDappTransactionFee())
                .put(Decoding.unsafeDecodeHex(getSenderPublicKey()))
                .putInt(getDappTransactionType())
                .put(getDappArgsBuffer());
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
        return message == null ? new byte[0] :Encoding.getUTF8Bytes( message);
    }

    private byte[] getDappTransactionFee(){

        return fee ==null?new byte[0]:String.valueOf(fee).getBytes();
    }

    private Integer getDappTransactionType(){
        return  contractType==null?null:contractType.getCode();
    }

    private byte[] getDappArgsBuffer(){
        return optionInfo==null?new byte[0]:optionInfo.getArgsJson().getBytes();
    }

    private byte[] getSenderIdBuffer(){
        return Encoding.getUTF8Bytes(senderId);
    }

    private byte[] getRequestorIdBuffer(){
        return Encoding.getUTF8Bytes(requestorId);
    }

    private byte[] getArgsBuffer(){
        return Encoding.getUTF8Bytes(JSON.toJSONString(getArgs()));
        //return Encoding.getUTF8Bytes(getArgsJson());
    }

//    public String getArgsJson(){
//        StringBuilder builder=new StringBuilder();
//        builder.append("[");
//        Object[] args=getArgs();
//        for (int i = 0; i < args.length; i++) {
//            String arg= args[i].toString();
//            builder.append(String.format("\"%s\"",arg));
//            if (i!=(args.length-1)){
//                builder.append(",");
//            }
//        }
//        builder.append("]");
//        //String argsJson = JSON.toJSONString(args);
//        return builder.toString();
//    }

    public TransactionInfo calcFee() {
        this.setFee(FeeCalculater.calcFee(this));
        return this;
    }

    private byte[] getSignaturesBuffer() { return Decoding.unsafeDecodeHex(getSignatures()[0]); }

//    private byte[] getArgsBuffer(){
//        byte[] result=new byte[0];
//
//        if (optionInfo!=null && optionInfo.getArgs()!=null && optionInfo.getArgs().length>0){
//            ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE)
//                    .order(ByteOrder.LITTLE_ENDIAN);
//            for (String arg:optionInfo.getArgs()){
//                buffer.put(arg.getBytes());
//            }
//            buffer.flip();
//            result = new byte[buffer.remaining()];
//            buffer.get(result);
//        }
//        return result;
//    }

}
