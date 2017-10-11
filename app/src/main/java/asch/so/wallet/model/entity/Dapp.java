package asch.so.wallet.model.entity;

import io.realm.RealmObject;

/**
 * Created by kimziv on 2017/10/11.
 */

public class Dapp extends RealmObject {
    private String category;
    private String name;
    private String description;
    private String tags;
    private String type;
    private String link;
    private String icon;
    private String delegates;
    private String unlockDelegates;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getDelegates() {
        return delegates;
    }

    public void setDelegates(String delegates) {
        this.delegates = delegates;
    }

    public String getUnlockDelegates() {
        return unlockDelegates;
    }

    public void setUnlockDelegates(String unlockDelegates) {
        this.unlockDelegates = unlockDelegates;
    }
}
