package asch.so.wallet.model.entity;

import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/9/27.
 */

public class CoreAsset extends BaseAsset {

    public CoreAsset() {
        this.setName(AschConst.CORE_COIN_NAME);
        this.setPrecision(AschConst.CORE_COIN_PRECISION);
        this.setDesc("xas is core asset");
        this.setType(TYPE_XAS);
    }
}
