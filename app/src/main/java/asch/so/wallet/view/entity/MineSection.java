package asch.so.wallet.view.entity;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by kimziv on 2017/10/23.
 */

public class MineSection extends SectionEntity<MineItem> {
    public MineSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MineSection(MineItem mineItem) {
        super(mineItem);
    }
}
