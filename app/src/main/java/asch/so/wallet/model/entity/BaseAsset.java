package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/9/27.
 */

/*
            "name": "CCTime.XCT",
            "desc": "Token for CCTime",
            "maximum": "1000000000000000000",
            "precision": 8,
            "strategy": "",
            "quantity": "100000000000000000",
            "height": 183,
            "issuerId": "3196144307608101364",
            "acl": 0,
            "writeoff": 0,
            "allowWriteoff": 0,
            "allowWhitelist": 0,
            "allowBlacklist": 0,
            "maximumShow": "10000000000",
            "quantityShow": "1000000000"
 */
public class BaseAsset {

    private String name;
    private String desc;
    private String maximum;
    private int precision;
    private String strategy;
    private String quantity;
    private long height;
    private String issuerId;
    private int acl;
    private int writeoff;
    private int allowWriteoff;
    private int allowWhitelist;
    private int allowBlacklist;
    private String maximumShow;
    private String quantityShow;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public int getAcl() {
        return acl;
    }

    public void setAcl(int acl) {
        this.acl = acl;
    }

    public int getWriteoff() {
        return writeoff;
    }

    public void setWriteoff(int writeoff) {
        this.writeoff = writeoff;
    }

    public int getAllowWriteoff() {
        return allowWriteoff;
    }

    public void setAllowWriteoff(int allowWriteoff) {
        this.allowWriteoff = allowWriteoff;
    }

    public int getAllowWhitelist() {
        return allowWhitelist;
    }

    public void setAllowWhitelist(int allowWhitelist) {
        this.allowWhitelist = allowWhitelist;
    }

    public int getAllowBlacklist() {
        return allowBlacklist;
    }

    public void setAllowBlacklist(int allowBlacklist) {
        this.allowBlacklist = allowBlacklist;
    }

    public String getMaximumShow() {
        return maximumShow;
    }

    public void setMaximumShow(String maximumShow) {
        this.maximumShow = maximumShow;
    }

    public String getQuantityShow() {
        return quantityShow;
    }

    public void setQuantityShow(String quantityShow) {
        this.quantityShow = quantityShow;
    }
}
