package asch.so.wallet.model.entity;

import com.alibaba.fastjson.JSONObject;

import so.asch.sdk.transaction.asset.AssetInfo;

/**
 * Created by kimziv on 2017/10/13.
 */

/*

{
    "id": "b3bc209d65fb9f945f7d9aeea8734aa7f111b84d2654c4b7dc89b4c21da4aa2b",
    "height": "128459",
    "blockId": "f57f68e6245c42e936e403806b318325efe7ffef8aaa08e632769f5ae1507e40",
    "type": 14,
    "timestamp": 40472637,
    "senderPublicKey": "2856bdb3ed4c9b34fd2bba277ffd063a00f703113224c88c076c0c58310dbec4",
    "senderId": "ANH2RUADqXs6HPbPEZXv4qM8DZfoj4Ry3M",
    "recipientId": "AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym",
    "amount": 0,
    "fee": 10000000,
    "signature": "95debd7b968779173219831261f9786ba5c72f3221b409aed0172fd00558686828c2e6bc6611c98e7ab8a8eb8081e8caf97636e05dccd62a95cc805ecd70c40b",
    "signSignature": "",
    "signatures": null,
    "confirmations": "89002",
    "args": null,
    "message": "",
    "asset": {}
}
 */

public class Transaction {
    private String id; //交易ID
    private String height;//区块高度
    private String blockId;//区块ID
    private int type;//交易类型
    private int timestamp;//时间戳
    private String senderPublicKey;//发送者公钥
    private String senderId;//发送者地址
    private String recipientId;
    private long amount;
    private long fee;
    private String signature;
    private String signSignature;
    private String signatures;
    private String confirmations;
    private String args;
    private String message;
    private String asset;
    private Asset assetInfo;

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

    public Asset getAssetInfo() {
        return assetInfo;
    }

    public void setAssetInfo(Asset assetInfo) {
        this.assetInfo = assetInfo;
    }
}
