package asch.io.wallet.model.entity;

import android.text.TextUtils;

/**
 * Created by kimziv on 2017/11/24.
 */

/*
        {
            "ip": "45.76.98.139",
            "port": 80,
            "state": 2,
            "os": "linux3.13.0-112-generic",
            "version": "1.3.4"
        },
 */
public class PeerNode {

    private String ip;
    private int port;
    private int state;
    private String os;
    private String version;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStaredIp(){
        if (TextUtils.isEmpty(ip))
            return "*.*.*.*";
       String[] parts= ip.split("\\.");
       return  String.format("*.*.%s.%s",parts[2],parts[3]);
    }
}
