package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/11/15.
 */

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import asch.so.wallet.AppConstants;
import asch.so.wallet.util.AppUtil;
import so.asch.sdk.impl.AschConst;

/**
 *
 {
 "success": true,
 "account": {
 "address": "AQUtaQb5oJcEn7zGugyDqpb4X64pTH2cq",
 "balance": 0,
 "publicKey": "bd1e78c5a10fbf1eca36b28bbb8ea85f320967659cbf1f7ff1603d0a368867b9",
 "secondSignature": "",
 "secondPublicKey": "",
 "multisignatures": "",
 "u_multisignatures": "",
 "lockHeight": 0
 },
 "latestBlock": {
 "height": 596221,
 "timestamp": 43680400
 },
 "version": {
 "version": "1.3.4",
 "build": "13:46:03 27/09/2017",
 "net": "testnet"
 }
 }

 {
 "success": true,
 "account": {
 "address": "AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym",
 "unconfirmedBalance": 989837314999993,
 "balance": 989837314999993,
 "publicKey": "91789ab7e4ef9615f296842017ee287f77f1ab27476df95000df32388f45a088",
 "unconfirmedSignature": false,
 "secondSignature": false,
 "secondPublicKey": "",
 "multisignatures": [],
 "u_multisignatures": [],
 "lockHeight": 0
 },
 "latestBlock": {
 "height": 596249,
 "timestamp": 43680680
 },
 "version": {
 "version": "1.3.4",
 "build": "10:14:05 22/10/2017",
 "net": "testnet"
 }
 }

 {
 "success": true,
 "account": {
 "address": "ALR1PPnVZ1Q3FYc4gXCvwnqfdMwUcfjeQq",
 "unconfirmedBalance": 8444370000000,
 "balance": 8444370000000,
 "publicKey": "3fdd82c68d9c052a643ef9232ef8a36fc5064669ad183a97eeb6810cd7c1e977",
 "unconfirmedSignature": false,
 "secondSignature": true,
 "secondPublicKey": "4139a72b29eddf7626891a411e22354ac3246aab8e78fc71fbb20a77f492353b",
 "multisignatures": [],
 "u_multisignatures": [],
 "lockHeight": 0
 },
 "latestBlock": {
 "height": 596261,
 "timestamp": 43680800
 },
 "version": {
 "version": "1.3.4",
 "build": "10:14:05 22/10/2017",
 "net": "testnet"
 }
 }
 */
public class FullAccount {

    private AccountInfo account;
    private BlockInfo latestBlock;
    private VersionInfo version;



    public AccountInfo getAccount() {
        return account;
    }

    public void setAccount(AccountInfo account) {
        this.account = account;
    }

    public BlockInfo getLatestBlock() {
        return latestBlock;
    }

    public void setLatestBlock(BlockInfo latestBlock) {
        this.latestBlock = latestBlock;
    }

    public VersionInfo getVersion() {
        return version;
    }

    public void setVersion(VersionInfo version) {
        this.version = version;
    }

    public static class AccountInfo{
        private String address;
        @JSONField(name="xas")
        private String balance;
        private String publicKey;
        private boolean unconfirmedSignature;
        private boolean secondSignature;
        private String secondPublicKey;
        private boolean isLocked;
        @JSONField(name="weight")
        private long lockedAmount;
        private long lockHeight;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }


        public BigDecimal getBalanceDecimalValue(){
            return AppUtil.decimalFromBigint(Long.parseLong(balance), AschConst.CORE_COIN_PRECISION);
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getBalance() {
            return balance;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public boolean isUnconfirmedSignature() {
            return unconfirmedSignature;
        }

        public void setUnconfirmedSignature(boolean unconfirmedSignature) {
            this.unconfirmedSignature = unconfirmedSignature;
        }

        public boolean isSecondSignature() {
            return secondSignature;
        }

        public void setSecondSignature(boolean secondSignature) {
            this.secondSignature = secondSignature;
        }

        public String getSecondPublicKey() {
            return secondPublicKey;
        }

        public void setSecondPublicKey(String secondPublicKey) {
            this.secondPublicKey = secondPublicKey;
        }

        public long getLockHeight() {
            return lockHeight;
        }

        public void setLockHeight(long lockHeight) {
            this.lockHeight = lockHeight;
        }

        public boolean checkTransferFee(){
         // BigDecimal decimal =  AppUtil.decimalFromBigint(Long.parseLong(balance), AppConstants.PRECISION);
           return AschConst.Fees.TRANSFER<Long.parseLong(balance);
        }

        public boolean checkUIATransferFee(){
            return AschConst.Fees.UIA_TRANSFER<Long.parseLong(balance);
        }

        public boolean checkVoteFee(){
            return AschConst.Fees.VOTE<Long.parseLong(balance);
        }

        public boolean checkSetSecondPwdFee(){
            return AschConst.Fees.SECOND_SIGNATURE<Long.parseLong(balance);
        }

        public boolean isLocked() {
            return isLocked;
        }

        public void setLocked(boolean locked) {
            isLocked = locked;
        }

        public long getLockedAmount() {
            return lockedAmount;
        }

        public void setLockedAmount(long lockedAmount) {
            this.lockedAmount = lockedAmount;
        }
    }

    public static class BlockInfo{
        private long height;
        private long timestamp;

        public long getHeight() {
            return height;
        }

        public void setHeight(long height) {
            this.height = height;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class VersionInfo{
        private String version;
        private String build;
        private String net;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getBuild() {
            return build;
        }

        public void setBuild(String build) {
            this.build = build;
        }

        public String getNet() {
            return net;
        }

        public void setNet(String net) {
            this.net = net;
        }
    }
}
