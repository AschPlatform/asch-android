package asch.so.wallet.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import asch.so.wallet.util.AppUtil;
import so.asch.sdk.AschSDK;

/**
 * Created by Deng on 2018 09 25
 */

public class IssuerAssets {
    private String name;

    private String tid;

    private int timestamp;

    private String maximum;

    private int precision;

    private String quantity;

    private String desc;

    private String issuerId;

    private int _version_;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setTid(String tid){
        this.tid = tid;
    }
    public String getTid(){
        return this.tid;
    }
    public void setTimestamp(int timestamp){
        this.timestamp = timestamp;
    }
    public int getTimestamp(){
        return this.timestamp;
    }
    public void setMaximum(String maximum){
        this.maximum = maximum;
    }
    public String getMaximum(){
        return this.maximum;
    }
    public void setPrecision(int precision){
        this.precision = precision;
    }
    public int getPrecision(){
        return this.precision;
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }
    public String getQuantity(){
        return this.quantity;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return this.desc;
    }
    public void setIssuerId(String issuerId){
        this.issuerId = issuerId;
    }
    public String getIssuerId(){
        return this.issuerId;
    }
    public void set_version_(int _version_){
        this._version_ = _version_;
    }
    public int get_version_(){
        return this._version_;
    }

    public Date dateFromAschTimestamp(){
        return   AschSDK.Helper.dateFromAschTimestamp(timestamp);
    }

}
