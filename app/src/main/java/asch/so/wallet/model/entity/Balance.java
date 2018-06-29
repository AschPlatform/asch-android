package asch.so.wallet.model.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

import asch.so.wallet.AppConstants;
import asch.so.wallet.util.AppUtil;

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


    public static class UIAAsset{
        private String name;
        private String tid;
        private int timestamp;
        private String maximum;
        private int precision;
        private String quantity;
        private String desc;

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
