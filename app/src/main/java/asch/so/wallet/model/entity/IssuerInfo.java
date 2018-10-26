package asch.so.wallet.model.entity;

public class IssuerInfo {

    private String tid;

    private String name;

    private String issuerId;

    private String desc;

    private int _version_;

    public void setTid(String tid){
        this.tid = tid;
    }
    public String getTid(){
        return this.tid;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setIssuerId(String issuerId){
        this.issuerId = issuerId;
    }
    public String getIssuerId(){
        return this.issuerId;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return this.desc;
    }
    public void set_version_(int _version_){
        this._version_ = _version_;
    }
    public int get_version_(){
        return this._version_;
    }

}
