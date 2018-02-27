package asch.so.wallet.miniapp.download;

import asch.so.wallet.model.entity.Dapp;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kimziv on 2018/1/22.
 */

public class TaskModel extends RealmObject {
    @PrimaryKey
    private String dappID;
    private int id;
    private String name;
    private String url;
    private String path;
    private Dapp dapp;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDappID() {
        return dappID;
    }

    public void setDappID(String dappID) {
        this.dappID = dappID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Dapp getDapp() {
        return dapp;
    }

    public void setDapp(Dapp dapp) {
        this.dapp = dapp;
    }
}
