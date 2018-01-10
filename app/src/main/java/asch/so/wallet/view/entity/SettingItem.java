package asch.so.wallet.view.entity;

/**
 * Created by kimziv on 2017/10/16.
 */

public class SettingItem extends BaseItem {
    private boolean hasArrow;

    public SettingItem(String title, boolean hasArrow) {
        super(title);
        this.hasArrow = hasArrow;
    }

    public boolean isHasArrow() {
        return hasArrow;
    }

    public void setHasArrow(boolean hasArrow) {
        this.hasArrow = hasArrow;
    }
}
