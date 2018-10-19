package asch.so.wallet.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import asch.so.wallet.util.AppUtil;
import so.asch.sdk.AschSDK;

/**
 * Created by Deng on 2018年09月25日
 */

public class GatewayAccount {

    String address;
    int seq;
    String gateway;
    String outAddress;
    int version;
    int createTime;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getOutAddress() {
        return outAddress;
    }

    public void setOutAddress(String outAddress) {
        this.outAddress = outAddress;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }
}
