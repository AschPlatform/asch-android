package asch.so.wallet.view.entity;

/**
 * Created by kimziv on 2017/9/28.
 */

public class MineItem {

    private String icon;
    private String title;

    public MineItem(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
