package asch.io.wallet.activity;

import android.os.Bundle;

import asch.io.base.util.ActivityUtils;
import asch.io.wallet.R;
import asch.io.wallet.view.fragment.BlockBrowseFragment;

/**
 * Created by haizeiwang on 2018/03/14.
 * 区块浏览
 */
public class BlockBrowseActivity extends TitleToolbarActivity {

    BlockBrowseFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.block_browse));

        fragment= BlockBrowseFragment.newInstance();

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.fragment_container);
    }


}
