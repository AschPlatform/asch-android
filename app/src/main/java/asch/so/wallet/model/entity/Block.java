package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/12/4.
 */

/**
 {
 "id": "07e60ec08d55b6c20cbc99d01ae66ec1e15eda4e787167a117b03835006d5f5b",
 "version": 0,
 "timestamp": 45321390,
 "height": 4077391,
 "previousBlock": "d8565612ea39b77c2a59c3d52fd1e36fee8281bc6ad41a138d8614b186a40be3",
 "numberOfTransactions": 0,
 "totalAmount": 0,
 "totalFee": 0,
 "reward": 300000000,
 "payloadLength": 0,
 "payloadHash": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
 "generatorPublicKey": "a1e84e18abb28535c90d91c57ba5d14954d3686f5be9b532f4bfd7d1a00a6588",
 "generatorId": "11055855660784579546",
 "blockSignature": "b103f8a3f9fde0eca9a6d3fdcdcfe24cde9f1a024b4766435e26dc33130319ea502d87e34aa799430cae804ff135aa14db865ca9bcc9fce1a825883b1eee1e0e",
 "confirmations": "1",
 "totalForged": 300000000
 }
 */
public class Block {
    private String id;
    private int timestamp;
    private int version;
    private int height;
    private String previousBlock;
    private int numberOfTransactions;
    private long totalAmount;
    private long totalFee;
    private long reward;
    private int payloadLength;
    private String payloadHash;
    private String generatorPublicKey;
    private String generatorId;
    private String blockSignature;
    private String confirmations;
    private long totalForged;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPreviousBlock() {
        return previousBlock;
    }

    public void setPreviousBlock(String previousBlock) {
        this.previousBlock = previousBlock;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(int numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(long totalFee) {
        this.totalFee = totalFee;
    }

    public long getReward() {
        return reward;
    }

    public void setReward(long reward) {
        this.reward = reward;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(int payloadLength) {
        this.payloadLength = payloadLength;
    }

    public String getPayloadHash() {
        return payloadHash;
    }

    public void setPayloadHash(String payloadHash) {
        this.payloadHash = payloadHash;
    }

    public String getGeneratorPublicKey() {
        return generatorPublicKey;
    }

    public void setGeneratorPublicKey(String generatorPublicKey) {
        this.generatorPublicKey = generatorPublicKey;
    }

    public String getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(String generatorId) {
        this.generatorId = generatorId;
    }

    public String getBlockSignature() {
        return blockSignature;
    }

    public void setBlockSignature(String blockSignature) {
        this.blockSignature = blockSignature;
    }

    public String getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(String confirmations) {
        this.confirmations = confirmations;
    }

    public long getTotalForged() {
        return totalForged;
    }

    public void setTotalForged(long totalForged) {
        this.totalForged = totalForged;
    }
}
