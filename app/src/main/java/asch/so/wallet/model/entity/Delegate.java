package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/11/29.
 */

/*
{
            "username": "thinker",
            "address": "18264520677606703363",
            "publicKey": "4e56eb22bb57fe42967521217d5006101ee6a507c4af3c9c1be94de87997f4bb",
            "balance": 14358418572184,
            "vote": 2229461030725921,
            "producedblocks": 40030,
            "missedblocks": 151,
            "fees": 71669168127,
            "rewards": 11688240000000,
            "rate": 1,
            "approval": 19.87,
            "productivity": 99.62,
            "forged": "11759909168127",
            "voted": true
        }
 */
public class Delegate {
    private String username;
    private String address;
    private String publicKey;
    private long balance;
    private long vote;
    private int producedblocks;
    private int missedblocks;
    private long fees;
    private long rewards;
    private int rate;
    private float approval;
    private float productivity;
    private long forged;
    private boolean voted;

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

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getVote() {
        return vote;
    }

    public void setVote(long vote) {
        this.vote = vote;
    }

    public int getProducedblocks() {
        return producedblocks;
    }

    public void setProducedblocks(int producedblocks) {
        this.producedblocks = producedblocks;
    }

    public int getMissedblocks() {
        return missedblocks;
    }

    public void setMissedblocks(int missedblocks) {
        this.missedblocks = missedblocks;
    }

    public long getFees() {
        return fees;
    }

    public void setFees(long fees) {
        this.fees = fees;
    }

    public long getRewards() {
        return rewards;
    }

    public void setRewards(long rewards) {
        this.rewards = rewards;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public float getApproval() {
        return approval;
    }

    public void setApproval(float approval) {
        this.approval = approval;
    }

    public float getProductivity() {
        return productivity;
    }

    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }

    public long getForged() {
        return forged;
    }

    public void setForged(long forged) {
        this.forged = forged;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }
}
