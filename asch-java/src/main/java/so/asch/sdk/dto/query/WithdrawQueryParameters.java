package so.asch.sdk.dto.query;


public class WithdrawQueryParameters extends QueryParameters {
    public String getAddress() {
        return address;
    }

    public WithdrawQueryParameters setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public WithdrawQueryParameters setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    private String address = null;
    private String currency=null;

}