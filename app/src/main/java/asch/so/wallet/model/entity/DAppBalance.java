package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2018/2/9.
 */


/*
{
		"currency": "CCTime.XCT",
		"balance": "13027783605370366",
		"maximum": "1000000000000000000",
		"precision": 8,
		"quantity": "100000000000000000",
		"writeoff": 0,
		"allowWriteoff": 0,
		"allowWhitelist": 0,
		"allowBlacklist": 0,
		"maximumShow": "10000000000",
		"quantityShow": "1000000000",
		"balanceShow": "130277836.05370366"
}
 */
public class DAppBalance extends Balance {
    private int allowWriteoff;
    private int allowWhitelist;
    private int allowBlacklist;
    private String maximumShow;
    private String quantityShow;
    private String balanceShow;

    public int getAllowWriteoff() {
        return allowWriteoff;
    }

    public void setAllowWriteoff(int allowWriteoff) {
        this.allowWriteoff = allowWriteoff;
    }

    public int getAllowWhitelist() {
        return allowWhitelist;
    }

    public void setAllowWhitelist(int allowWhitelist) {
        this.allowWhitelist = allowWhitelist;
    }

    public int getAllowBlacklist() {
        return allowBlacklist;
    }

    public void setAllowBlacklist(int allowBlacklist) {
        this.allowBlacklist = allowBlacklist;
    }

    public String getMaximumShow() {
        return maximumShow;
    }

    public void setMaximumShow(String maximumShow) {
        this.maximumShow = maximumShow;
    }

    public String getQuantityShow() {
        return quantityShow;
    }

    public void setQuantityShow(String quantityShow) {
        this.quantityShow = quantityShow;
    }

    public String getBalanceShow() {
        return balanceShow;
    }

    public void setBalanceShow(String balanceShow) {
        this.balanceShow = balanceShow;
    }
}
