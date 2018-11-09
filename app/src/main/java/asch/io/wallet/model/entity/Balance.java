package asch.io.wallet.model.entity;

import android.support.annotation.IntDef;

import com.alibaba.fastjson.annotation.JSONField;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

import asch.io.wallet.AppConstants;
import asch.io.wallet.util.AppUtil;

/**
 * Created by kimziv on 2017/9/28.
 */

/*
        "currency": "zhenxi.UIA",
		"balance": "900000",
		"maximum": "10000000",
		"precision": 3,
		"quantity": "1000000",
		"writeoff": 1
 */


public class Balance {


    @IntDef({STATE_UNSETTING,STATE_SHOW,STATE_UNSHOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}
    public static final int STATE_UNSETTING = 0;
    public static final int STATE_SHOW = 1;
    public static final int STATE_UNSHOW = 2;
    //显示状态
    public @State int state = 0;
    public int getState() {
        return state;
    }
    public void setState(@State int s) {
        this.state = s;
    }



    private String address;
    private String currency;
    private String balance;
    private String maximum;
    private int precision;
    private String quantity;
    private int writeoff;
    private int flag;
    @JSONField(name="asset")
    private String assetJson;
    private UIAAsset uiaAsset;
    private GatewayAsset gatewayAsset;
    private @BaseAsset.Type int type = BaseAsset.TYPE_XAS;

    public static class UIAAsset{
        private String name;
        private String tid;
        private int timestamp;
        private String maximum;
        private int precision;
        private String quantity;
        private String desc;
        private String issuerId;
        private int _version_;

        public String getIssuerId() {
            return issuerId;
        }

        public void setIssuerId(String issuerId) {
            this.issuerId = issuerId;
        }

        public int get_version_() {
            return _version_;
        }

        public void set_version_(int _version_) {
            this._version_ = _version_;
        }

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


    }

    public static class GatewayAsset {
        private String gateway;
        private String symbol;
        private String desc;
        private int precision;
        private int revoked;

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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getPrecision() {
            return precision;
        }

        public void setPrecision(int precision) {
            this.precision = precision;
        }

        public int getRevoked() {
            return revoked;
        }

        public void setRevoked(int revoked) {
            this.revoked = revoked;
        }


    }

    public AschAsset gatewayToAschAsset(){
        AschAsset aschAsset = new AschAsset();
        aschAsset.setName(gatewayAsset.symbol);
        aschAsset.setDesc(gatewayAsset.desc);
        aschAsset.setPrecision(gatewayAsset.precision);
        aschAsset.setRevoked(gatewayAsset.revoked);
        aschAsset.setGateway(gatewayAsset.gateway);
        aschAsset.setAddress(address);
        aschAsset.setBalance(balance);
        aschAsset.setFlag(flag);
        aschAsset.setType(AschAsset.TYPE_GATEWAY);
        aschAsset.setAidFromAsset(aschAsset);
        aschAsset.setTrueBalance(getRealBalance());
        return aschAsset;
    }

    public AschAsset uiaToAschAsset(){
        AschAsset aschAsset = new AschAsset();
        aschAsset.setName(uiaAsset.name);
        aschAsset.setTid(uiaAsset.tid);
        aschAsset.setTimestamp(uiaAsset.timestamp);
        aschAsset.setMaximum(uiaAsset.maximum);
        aschAsset.setPrecision(uiaAsset.precision);
        aschAsset.setQuantity(uiaAsset.quantity);
        aschAsset.setDesc(uiaAsset.desc);
        aschAsset.setIssuerId(uiaAsset.issuerId);
        aschAsset.setVersion(uiaAsset._version_);
        aschAsset.setType(AschAsset.TYPE_UIA);
        aschAsset.setFlag(flag);
        aschAsset.setAddress(address);
        aschAsset.setBalance(balance);
        aschAsset.setAidFromAsset(aschAsset);
        aschAsset.setTrueBalance(getRealBalance());
        return aschAsset;
    }

    public AschAsset toAschAsset(){
        if (flag==3){
            return gatewayToAschAsset();
        }else if(flag ==2){
            return uiaToAschAsset();
        }else
            return new AschAsset();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {

        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public int getWriteoff() {
        return writeoff;
    }

    public void setWriteoff(int writeoff) {
        this.writeoff = writeoff;
    }

    public float getRealBalance(){
       return (float) (Double.parseDouble(balance)/(Math.pow(10,precision)));
    }

    public long getLongBalance(){
        return  Long.parseLong(balance);
    }
    public BigDecimal getDecimalBalance(){
        return AppUtil.decimalFromBigint(Long.parseLong(balance),precision);
    }

    public String getBalanceString(){
        long longBalance=Long.parseLong(balance);
        int preci=0;
        if (this.getFlag()== AppConstants.UIA_FLAG)
        {
            preci=this.uiaAsset.getPrecision();
        }else if(this.getFlag()== AppConstants.GATEWAY_FLAG){
            preci=this.getGatewayAsset().getPrecision();
        }else {
            preci=this.getPrecision();
        }
        return longBalance==0?"0":AppUtil.decimalFormat(AppUtil.decimalFromBigint(longBalance,preci));
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAssetJson() {
        return assetJson;
    }

    public void setAssetJson(String assetJson) {
        this.assetJson = assetJson;
    }

    public UIAAsset getUiaAsset() {
        return uiaAsset;
    }

    public void setUiaAsset(UIAAsset uiaAsset) {
        this.uiaAsset = uiaAsset;
    }

    public GatewayAsset getGatewayAsset() {
        return gatewayAsset;
    }

    public void setGatewayAsset(GatewayAsset gatewayAsset) {
        this.gatewayAsset = gatewayAsset;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


}
