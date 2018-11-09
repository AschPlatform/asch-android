package asch.io.wallet.model.entity;

/**
 * 把uiaAsset与gatewayAsset统一
 */

public class UserAsset extends BaseAsset{


//    private String aid;

    /**
     * uia
     */
    private String tid;
    private int timestamp;
    private int _version_;
//    public AschAsset toAschAsset(){
//        AschAsset aschAsset = new AschAsset();
//        aschAsset.setName(getName());
//        aschAsset.setTid(getTid());
//        aschAsset.setType(AschAsset.TYPE_UIA);
//        aschAsset.setMaximum(getMaximum());
//        aschAsset.setPrecision(getPrecision());
//        aschAsset.setQuantity(getQuantity());
//        aschAsset.setDesc(getDesc());
//        aschAsset.setIssuerId(getIssuerId());
//        aschAsset.setVersion(get_version_());
//        aschAsset.setTimestamp(getTimestamp());
//        aschAsset.setAidFromAsset(aschAsset);
//        aschAsset.setBalance("0");
//        return aschAsset;
//    }

    /**
     * gateway
     */
    private String gateway;
    private String symbol;
    private String desc;
    private int precision;
    private int revoked;
    //    public AschAsset toAschAsset(){
//        AschAsset aschAsset = new AschAsset();
//        aschAsset.setName(getSymbol());
//        aschAsset.setDesc(getDesc());
//        aschAsset.setPrecision(getPrecision());
//        aschAsset.setRevoked(getRevoked());
//        aschAsset.setType(AschAsset.TYPE_GATEWAY);
//        aschAsset.setGateway(getGateway());
//        aschAsset.setAidFromAsset(aschAsset);
//        aschAsset.setBalance("0");
//        return aschAsset;
//    }

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
    public UserAsset(){
        this.setType(TYPE_UIA);
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int get_version_() {
        return _version_;
    }

    public void set_version_(int _version_) {
        this._version_ = _version_;
    }


}
