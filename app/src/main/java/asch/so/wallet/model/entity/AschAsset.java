package asch.so.wallet.model.entity;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import asch.so.wallet.AppConstants;
import asch.so.wallet.util.AppUtil;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import so.asch.sdk.Gateway;

public class AschAsset extends RealmObject {


    @IntDef({STATE_UNSETTING,STATE_SHOW,STATE_UNSHOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}
    public static final int STATE_UNSETTING = 0;
    public static final int STATE_SHOW = 1;
    public static final int STATE_UNSHOW = 2;
    private @State int showState;

    private String address;

    @PrimaryKey
    private String aid;
    private int type;
    private String name;
    private String desc;
    private int precision;
    private String balance;
    private float trueBalance;

    private String tid;
    private String maximum;
    private String quantity;
    private String issuerId;
    private long timestamp;
    private int version;

    private String gateway;
    private String symbol;
    private int revoked;
    private int flag;


    private int createTimestamp;
    private int updateTimestamp;

    private String strategy;
    private long height;
    private int acl;
    private int writeoff;
    private int allowWriteoff;
    private int allowWhitelist;
    private int allowBlacklist;
    private String maximumShow;
    private String quantityShow;
    private String xasTotal;



    public void setAidFromAsset(AschAsset asset){
        if(asset.getType()==AschAsset.TYPE_GATEWAY){
            aid =asset.name+"_"+String.valueOf(asset.getType())+"_"+asset.getGateway();
        }
        else if (asset.getType()==AschAsset.TYPE_UIA)
            aid = asset.getName()+"_"+String.valueOf(asset.getType())+"_"+asset.getTid();
        else
            aid = asset.getName();
    }

    public void setTrueBalance(float trueBalance) {
        this.trueBalance = trueBalance;
    }

    public float getTrueBalance(){
        return this.trueBalance;
    }

    public String getXasTotal() {
        return xasTotal;
    }

    public void setXasTotal(String xasTotal) {
        this.xasTotal = xasTotal;
    }


//    public Balance toBalance(){
//        Balance balance = new Balance();
//        balance.setPrecision(precision);
//        balance.setType(type);
//        balance.setCurrency(name);
//
//        return balance;
//    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getShowState() {
        return showState;
    }

    public void setShowState(int showState) {
        this.showState = showState;
    }

    public int getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(int createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public int getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(int updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @IntDef({TYPE_XAS,TYPE_UIA,TYPE_GATEWAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {}
    public static final int TYPE_XAS = 0;
    public static final int TYPE_UIA = 1;
    public static final int TYPE_GATEWAY = 2;

    public void setType(@Type int type) {
        this.type = type;
    }

    public @Type int getType() {
        return type;
    }

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

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getRevoked() {
        return revoked;
    }

    public void setRevoked(int revoked) {
        this.revoked = revoked;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getBalanceString(){
        long longBalance=Long.parseLong(balance);
        int preci=0;
        preci=this.getPrecision();
        return longBalance==0?"0":AppUtil.decimalFormat(AppUtil.decimalFromBigint(longBalance,preci));
    }

    public long getLongBalance(){
        long longBalance=Long.parseLong(balance);
        return longBalance;
    }



}
