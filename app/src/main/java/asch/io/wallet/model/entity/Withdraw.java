package asch.io.wallet.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import asch.io.wallet.util.AppUtil;
import so.asch.sdk.AschSDK;

/**
 * Created by Deng on 2018 09 25
 */

public class Withdraw {
    String tid;
    int timestamp;
    String _version_;
    String gateway;
    String senderId;
    String recipientId;
    String currency;
    int seq;
    long amount;
    String fee;
    int signs;
    int ready;
    int oid;

    WithdrawAsset asset;

    public WithdrawAsset getAsset() {
        return asset;
    }

    public void setAsset(WithdrawAsset asset) {
        this.asset = asset;
    }


    //    String outTransaction;
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

//    class outTransaction{
//        String txhex;
//        ArrayList<String>input;
//
//        public String getTxhex() {
//            return txhex;
//        }
//
//        public void setTxhex(String txhex) {
//            this.txhex = txhex;
//        }
//
//        public ArrayList<String> getInput() {
//            return input;
//        }
//
//        public void setInput(ArrayList<String> input) {
//            this.input = input;
//        }
//    };


    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public int getSigns() {
        return signs;
    }

    public void setSigns(int signs) {
        this.signs = signs;
    }

    public int getReady() {
        return ready;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String get_version_() {
        return _version_;
    }

    public void set_version_(String _version_) {
        this._version_ = _version_;
    }



}
