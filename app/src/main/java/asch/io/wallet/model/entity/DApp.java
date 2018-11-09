package asch.io.wallet.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kimziv on 2017/10/11.
 * {
 "name": "asch-dapp-cctime",
 "description": "Decentralized news channel",
 "tags": "asch,dapp,demo,cctime",
 "link": "https://github.com/AschPlatform/asch-dapp-cctime/archive/master.zip",
 "type": 0,
 "category": 1,
 "icon": "http://o7dyh3w0x.bkt.clouddn.com/hello.png",
 "delegates": ["8b1c24a0b9ba9b9ccf5e35d0c848d582a2a22cca54d42de8ac7b2412e7dc63d4", "aa7dcc3afd151a549e826753b0547c90e61b022adb26938177904a73fc4fee36", "e29c75979ac834b871ce58dc52a6f604f8f565dea2b8925705883b8c001fe8ce", "55ad778a8ff0ce4c25cb7a45735c9e55cf1daca110cfddee30e789cb07c8c9f3", "982076258caab20f06feddc94b95ace89a2862f36fea73fa007916ab97e5946a"],
 "unlockDelegates": 3,
 "transactionId": "d352263c517195a8b612260971c7af869edca305bb64b471686323817e57b2c1"
 }
 */

public class DApp extends RealmObject{
    private int category;
    private String name;
    private String description;
    private String tags;
    private int type;
    private String link;
    private String icon;
    //private String[] delegates;
    private int unlockDelegates;
    @PrimaryKey
    private String transactionId;

    //下载管理，新增字段
    private  int downloadId;
    private  String downloadUrl;
    private  String downloadPath;
    private  String installedPath;
    private int status;
    private long sofarBytes;
    private long totalBytes;
    private String verion;
    private int build;
    private int publishTimestamp;
    private int updateTimestamp;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

//    public String[] getDelegates() {
//        return delegates;
//    }
//
//    public void setDelegates(String[] delegates) {
//        this.delegates = delegates;
//    }

    public int getUnlockDelegates() {
        return unlockDelegates;
    }

    public void setUnlockDelegates(int unlockDelegates) {
        this.unlockDelegates = unlockDelegates;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getInstalledPath() {
        return installedPath;
    }

    public void setInstalledPath(String installedPath) {
        this.installedPath = installedPath;
    }

    public String getVerion() {
        return verion;
    }

    public void setVerion(String verion) {
        this.verion = verion;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPublishTimestamp() {
        return publishTimestamp;
    }

    public void setPublishTimestamp(int publishTimestamp) {
        this.publishTimestamp = publishTimestamp;
    }

    public int getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(int updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public long getSofarBytes() {
        return sofarBytes;
    }

    public void setSofarBytes(long sofarBytes) {
        this.sofarBytes = sofarBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }
}
