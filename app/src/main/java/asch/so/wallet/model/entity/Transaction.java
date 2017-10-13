package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/10/13.
 */

public class Transaction {
    private String id;
    private long amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
