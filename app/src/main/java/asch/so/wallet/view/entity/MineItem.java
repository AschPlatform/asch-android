package asch.so.wallet.view.entity;

/**
 * Created by kimziv on 2017/9/28.
 */

public class MineItem extends BaseItem{

    private String icon;

    public MineItem(String icon, String title) {
        super(title);
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
