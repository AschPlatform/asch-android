package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/9/27.
 */

public class GatewayAsset extends BaseAsset{

    public GatewayAsset(){
        this.setType(TYPE_GATEWAY);
    }

    private String gateway;
    private String symbol;
    private String desc;
    private int precision;
    private int revoked;

    public AschAsset toAschAsset(){
        AschAsset aschAsset = new AschAsset();
        aschAsset.setName(getSymbol());
        aschAsset.setDesc(getDesc());
        aschAsset.setPrecision(getPrecision());
        aschAsset.setRevoked(getRevoked());
        aschAsset.setType(AschAsset.TYPE_GATEWAY);
        aschAsset.setGateway(getGateway());
        aschAsset.setAidFromAsset(aschAsset);
        aschAsset.setBalance("0");
        return aschAsset;
    }
    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getRevoked() {
        return revoked;
    }

    public void setRevoked(int revoked) {
        this.revoked = revoked;
    }


}
