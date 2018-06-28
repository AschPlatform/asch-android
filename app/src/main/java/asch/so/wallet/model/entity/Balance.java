package asch.so.wallet.model.entity;

import java.math.BigDecimal;

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
    private Asset asset;

    public static class Asset{
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
        return longBalance==0?"0":AppUtil.decimalFormat(AppUtil.decimalFromBigint(longBalance,precision));
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
}
