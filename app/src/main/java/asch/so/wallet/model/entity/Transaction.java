package asch.so.wallet.model.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import asch.so.wallet.util.AppUtil;
import so.asch.sdk.AschSDK;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/10/13.
 */

/*
{
	"success": true,
	"count": 3,
	"transfers": [{
		"tid": "343cfc15d931554f2f276846597b31fbeaf80774aed518643918f575dcebd915",
		"senderId": "A7jbxyRqsaVQRbY69rzKbVruc8tnamdAsf",
		"recipientId": "AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym",
		"recipientName": "",
		"currency": "hhh.OOPP",
		"amount": "1000000000",
		"timestamp": 63001055,
		"_version_": 1,
		"asset": {
			"name": "hhh.OOPP",
			"tid": "fb5a25985dd30b15a9ad9b7addc957f942b2332a3792aa3be6da39e099cfcf06",
			"timestamp": 62518008,
			"maximum": "2000000000000",
			"precision": 6,
			"quantity": "2000000000000",
			"desc": "黄金季节",
			"issuerId": "A6MGhEcA8xgnUiGbszKrbtVjtxQ22ySx4m",
			"_version_": 2
		},
		"transaction": {
			"id": "343cfc15d931554f2f276846597b31fbeaf80774aed518643918f575dcebd915",

			}		"type": 103,
			"timestamp": 63001055,
			"senderId": "A7jbxyRqsaVQRbY69rzKbVruc8tnamdAsf",
			"senderPublicKey": "ed6b73946e78ac5ef795fa7887e21a1e65b6191caef95115e974d0dd99057cc0",
			"requestorId": null,
			"fee": 10000000,
			"signatures": "[\"069849f36bd3c17e7a20a4b2a84506f1c5cfa562ddd3a7ab514d4796f8487188ecc2a8405b6a79bc4c960894bce9e4bb132ddebe41d66ffa3349f914bdbc3407\"]",
			"secondSignature": "197d71194c5206cf4d85c609c2dd85ce68664a49e4817ac8dd1682afffb0925255fb4a8ef57e02c9c9880dec58399f3473b9c41296cec57a568d5f38ec7bfb05",
			"args": "[\"hhh.OOPP\",\"1000000000\",\"AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym\"]",
			"height": 67259,
			"message": "",
			"executed": 1,
			"_version_": 1
		}
	}

 */

public class Transaction {
    @JSONField(name="tid")
    private String id; //交易ID
    @JSONField(name="t_height")
    private String height;//区块高度
    private String blockId;//区块ID
    @JSONField(name="t_type")
    private int type;//交易类型
    private int timestamp;//时间戳
    private String senderPublicKey;//发送者公钥
    private String senderId;//发送者地址
    private String recipientId;
    private String recipientName;
    private String currency;
    private long amount;
    private long fee;
    private String signature;
    private String signSignature;
    private String signatures;
    private String confirmations;
    private String args;
    private String message;
    private String asset;
    private AssetInfo assetInfo;
    private TransactionInfo transaction;

    /**
     "transaction": {
     "id": "343cfc15d931554f2f276846597b31fbeaf80774aed518643918f575dcebd915",
     "type": 103,
     "timestamp": 63001055,
     "senderId": "A7jbxyRqsaVQRbY69rzKbVruc8tnamdAsf",
     "senderPublicKey": "ed6b73946e78ac5ef795fa7887e21a1e65b6191caef95115e974d0dd99057cc0",
     "requestorId": null,
     "fee": 10000000,
     "signatures": "[\"069849f36bd3c17e7a20a4b2a84506f1c5cfa562ddd3a7ab514d4796f8487188ecc2a8405b6a79bc4c960894bce9e4bb132ddebe41d66ffa3349f914bdbc3407\"]",
     "secondSignature": "197d71194c5206cf4d85c609c2dd85ce68664a49e4817ac8dd1682afffb0925255fb4a8ef57e02c9c9880dec58399f3473b9c41296cec57a568d5f38ec7bfb05",
     "args": "[\"hhh.OOPP\",\"1000000000\",\"AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym\"]",
     "height": 67259,
     "message": "",
     "executed": 1,
     "_version_": 1
     }
     */
    public  static  class  TransactionInfo{
        private String id;
        private int type;
        private int timestamp;
        private String senderId;
        private String senderPublicKey;
        private String requestorId;
        private long fee;
        private String issuerId;
        @JSONField(name="_version_")
        private int version;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getSenderPublicKey() {
            return senderPublicKey;
        }

        public void setSenderPublicKey(String senderPublicKey) {
            this.senderPublicKey = senderPublicKey;
        }

        public String getRequestorId() {
            return requestorId;
        }

        public void setRequestorId(String requestorId) {
            this.requestorId = requestorId;
        }

        public long getFee() {
            return fee;
        }

        public void setFee(long fee) {
            this.fee = fee;
        }

        public String getIssuerId() {
            return issuerId;
        }

        public void setIssuerId(String issuerId) {
            this.issuerId = issuerId;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }
    /**
     "asset": {
     "name": "hhh.OOPP",
     "tid": "fb5a25985dd30b15a9ad9b7addc957f942b2332a3792aa3be6da39e099cfcf06",
     "timestamp": 62518008,
     "maximum": "2000000000000",
     "precision": 6,
     "quantity": "2000000000000",
     "desc": "黄金季节",
     "issuerId": "A6MGhEcA8xgnUiGbszKrbtVjtxQ22ySx4m",
     "_version_": 2
     }
     */
    public static class AssetInfo{
       private String name;
       private String tid;
       private int timestamp;
       private String maximum;
       private int precision;
       private String quantity;
       private String desc;
       private String issuerId;
       @JSONField(name="_version_")
       private int version;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getMaximum() {
            return maximum;
        }

        public void setMaximum(String maximum) {
            this.maximum = maximum;
        }

        public int getPrecision() {
            return precision;
        }

        public void setPrecision(int precision) {
            this.precision = precision;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIssuerId() {
            return issuerId;
        }

        public void setIssuerId(String issuerId) {
            this.issuerId = issuerId;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }



    public enum Type{
        Transfer(0,"普通转账"),
        Signature(1, "设置二级密码"),
        Delegate(2, "注册受托人"),
        Vote(3, "投票"),
        MultiSignature(4, "多重签名"),
        Dapp(5, "DApp"),
        InTransfer(6,"Dapp充值"),
        OutTransfer(7,"Dapp提现"),
        Store(8, "小文件存储"),

        UIAIssuer(9, "发行商注册"),
        UIAAsset(10, "资产注册"),
        UIAFlags(11, "设置ACL模式"),
        UIA_ACL(12, "更新ACL访问控制列表"),
        UIAIssue(13, "资发行产"),
        UIATransfer(14, "主链UIA转账"),

        //new in V1.3
        Lock(100, "锁仓"),

        TransferV2(1,"transfer"),
        SignatureV2(3, "setSignature"),
        DelegateV2(10, "delegate"),
        VoteV2(3, "vote"),
        //MultiSignatureV2(4, "setMultiSignature"),
        //DappV2(200, "dapp"),
        InTransferV2(204, "inTransfer"),
        OutTransferV2(205,"outTransfer"),
        //StoreV2(8, "store"),

        UIAIssuerV2(100, "UIA_ISSUER"),
        UIAAssetV2(101, "UIA_ASSET"),
        //UIAFlagsV2(11, "UIA_FLAGS"),
        //UIA_ACLV2(12, "UIA_ACL"),
        UIAIssueV2(102, "UIA_ISSUE"),
        UIATransferV2(103, "UIA_TRANSFER"),

        //new in V1.3
        LockV2(4, "Lock");

        private static final Map<Integer, Type> allTransactionTypes = new HashMap<>();
        static{
            for( Type type : Type.values()){
                allTransactionTypes.put(type.getCode(), type);
            }
        }

        private int code;
        private String name;



        Type(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static Type fromCode(int code) {
            return allTransactionTypes.get(code);
        }
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Date dateFromAschTimestamp(){
      return   AschSDK.Helper.dateFromAschTimestamp(timestamp);
    }

    public long timestampFromAschTimestamp(){
        return   AschSDK.Helper.timestampFromAschTimestamp(timestamp);
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignSignature() {
        return signSignature;
    }

    public void setSignSignature(String signSignature) {
        this.signSignature = signSignature;
    }

    public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    public String getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(String confirmations) {
        this.confirmations = confirmations;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public AssetInfo getAssetInfo() {
        return assetInfo;
    }

    public void setAssetInfo(AssetInfo assetInfo) {
        this.assetInfo = assetInfo;
    }

    public TransactionInfo getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionInfo transaction) {
        this.transaction = transaction;
    }

    public String getBanlanceShow(boolean isSender){
        if ( TransactionType.TransferV2.getCode()==getType()){
            BigDecimal decimal = AppUtil.decimalFromBigint(getAmount(), AschConst.CORE_COIN_PRECISION);
            return   String.format("%s%s", isSender?"-":"+", AppUtil.decimalFormat(decimal)+" XAS");
        }else if (TransactionType.UIATransferV2.getCode()==getType()){
            //AssetInfo asset=(AssetInfo)getAssetInfo();
            //return asset.getQuantity();
            BigDecimal decimal = AppUtil.decimalFromBigint(getAmount(), this.assetInfo.getPrecision());
         return String.format("%s%s", isSender?"-":"+",AppUtil.decimalFormat(decimal)+" "+this.assetInfo.getName());
        }
        return "0 XAS";
    }



}
