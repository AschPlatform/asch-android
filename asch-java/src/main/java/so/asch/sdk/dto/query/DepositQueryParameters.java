package so.asch.sdk.dto.query;

import so.asch.sdk.TransactionType;



public class DepositQueryParameters extends QueryParameters {


    public DepositQueryParameters setAddress(String address) {
        this.address = address;
        return this;
    }



    public DepositQueryParameters setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public String getCurrency() {
        return currency;
    }

    private String address = null;
    private String currency=null;

}