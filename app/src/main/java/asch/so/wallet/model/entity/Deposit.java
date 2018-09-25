package asch.so.wallet.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import asch.so.wallet.util.AppUtil;
import so.asch.sdk.AschSDK;

/**
 * Created by Deng on 2018年09月25日
 */

public class Deposit {

    String tid;
    String currency;
    long amount;
    String address;
    int confirmations;
    int processed;
    String oid;
    int timestamp;
    asset asset;


    public class asset{
        String gateway;
        String symbol;
        String desc;
        int precision;
        String revoked;
        String _version_;

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

        public String getRevoked() {
            return revoked;
        }

        public void setRevoked(String revoked) {
            this.revoked = revoked;
        }

        public String get_version_() {
            return _version_;
        }

        public void set_version_(String _version_) {
            this._version_ = _version_;
        }
    }
    public String getBanlanceShow(){
        if (asset==null)
            return "";
        BigDecimal decimal = AppUtil.decimalFromBigint(getAmount(),this.asset.getPrecision());
        return String.format(AppUtil.decimalFormat(decimal)+" "+this.asset.getSymbol());
    }

    public Date dateFromAschTimestamp(){
        return   AschSDK.Helper.dateFromAschTimestamp(timestamp);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }


}
