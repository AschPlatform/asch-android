package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/9/30.
 */

public class QRCodeURL {
    private static  final  String REQUEST_PAY_PATH="/asch/pay/";
    private  String path;
    private String address;
    private String currency;
    private String amount;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String encodeQRCodeURL(){
        return String.format(REQUEST_PAY_PATH+address+"?currency=%$&&amount=%$",currency,amount);
    }

    public void decodeQRCodeURL(String url){
        // TODO: 2017/10/9
    }
}
