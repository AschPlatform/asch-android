package asch.io.wallet.model.entity;

import java.math.BigDecimal;

import asch.io.wallet.util.AppUtil;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/11/29.
 */
/*
{
    "username": "",
    "address": "11014282950707095123",
    "publicKey": "35bec75f66306b58fefa1ce6bd2f632855ed6f13d32577889d97a0614a5e0895",
    "balance": 0,
    "weight": 0
}
 */
public class Voter {
    private String username;
    private String address;
    private String publicKey;
    private long balance;
    private float weight;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public long getBalance() {
        return balance;
    }

    public String getBalanceShow() {
        return balance==0?"0": AppUtil.decimalFormat(AppUtil.decimalFromBigint(balance, AschConst.CORE_COIN_PRECISION));
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public float getWeight() {
        return weight;
    }

    public String getWeightShow(){
        BigDecimal w = new BigDecimal(Float.toString(weight));
      return   AppUtil.decimalFormat(w);
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
