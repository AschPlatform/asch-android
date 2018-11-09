package asch.io.wallet.view.entity;

/**
 * Created by kimziv on 2017/9/28.
 */

public class MineItem extends BaseItem{

    private int icon;

    public MineItem(int icon, String title) {
        super(title);
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
