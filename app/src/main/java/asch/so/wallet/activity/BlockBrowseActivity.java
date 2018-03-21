package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.view.fragment.BlockBrowseFragment;

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
