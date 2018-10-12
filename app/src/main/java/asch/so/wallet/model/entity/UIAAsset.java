package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/9/27.
 */

public class UIAAsset extends BaseAsset{

    private String tid;
    private int timestamp;
    private int _version_;

    public UIAAsset(){
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

    public AschAsset toAschAsset(){
        AschAsset aschAsset = new AschAsset();
        aschAsset.setName(getName());
        aschAsset.setTid(getTid());
        aschAsset.setType(AschAsset.TYPE_UIA);
        aschAsset.setMaximum(getMaximum());
        aschAsset.setPrecision(getPrecision());
        aschAsset.setQuantity(getQuantity());
        aschAsset.setDesc(getDesc());
        aschAsset.setIssuerId(getIssuerId());
        aschAsset.setVersion(get_version_());
        aschAsset.setTimestamp(getTimestamp());
        aschAsset.setAidFromAsset(aschAsset);
        aschAsset.setBalance("0");
        return aschAsset;
    }
}
