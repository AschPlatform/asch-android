package asch.so.wallet.view.entity;

/**
 * Created by kimziv on 2017/10/16.
 */

public abstract class BaseItem {
    private String title;

    public BaseItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
