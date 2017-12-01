package asch.so.wallet.model.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimziv on 2017/9/30.
 */

public class QRCodeURL {
    private static  final  String REQUEST_PAY_SCHEME="/asch/pay/";
    private  String path;
    private String address;
    private String currency;
    private String amount;
    //<scheme><address>?<query>
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
        return String.format(REQUEST_PAY_SCHEME+address+"?currency=%s&amount=%s",currency,amount);
    }

    public static QRCodeURL decodeQRCodeURL(String url) throws UnsupportedEncodingException{
        if (url!=null){
            //int start=0;
            QRCodeURL codeURL= new QRCodeURL();
            if (url.startsWith(REQUEST_PAY_SCHEME)){
              int mid1=REQUEST_PAY_SCHEME.length();
              int mid2=url.indexOf('?',mid1);
               String address=url.substring(mid1,mid2);
                String query=url.substring(mid2+1);
                final  Map<String, String> queryPairs=new HashMap<String, String>();
               final String [] pairs = query.split("&");
                for (String pair : pairs){
                    final int idx = pair.indexOf("=");
                    final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                    if (!queryPairs.containsKey(key)) {
                        final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                        queryPairs.put(key, value);
                    }
                }
                String  currency=queryPairs.get("currency");
                String  amount=queryPairs.get("amount");
                codeURL.setAddress(address);
                codeURL.setCurrency(currency);
                codeURL.setAmount(amount);
                return codeURL;

            }else {
                codeURL.setAddress(url);
                return codeURL;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "address: "+address+"\ncurrency: "+currency+"\namount:"+amount;
    }
}
