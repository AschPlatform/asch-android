package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.FullAccount;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/11/24.
 */

public class BlockInfoFragment extends BaseFragment {
    @BindView(R.id.block_height_val)
    TextView blockHeightTv;
    @BindView(R.id.block_time_val)
    TextView blockTimeTv;
    @BindView(R.id.verion_val)
    TextView versionTv;
    @BindView(R.id.buld_val)
    TextView buildTv;
    @BindView(R.id.net_val)
    TextView netTv;

    public static BlockInfoFragment newInstance() {
        
        Bundle args = new Bundle();
        
        BlockInfoFragment fragment = new BlockInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_block_info,container,false);
        ButterKnife.bind(this,rootView);
        showBlockInfo();
        return rootView;
    }

    private void showBlockInfo(){

        FullAccount account= AccountsManager.getInstance().getCurrentAccount().getFullAccount();
        if (account!=null){
            FullAccount.BlockInfo blockInfo =account.getLatestBlock();
            FullAccount.VersionInfo versionInfo =account.getVersion();

            blockHeightTv.setText(String.valueOf(blockInfo.getHeight()));
            blockTimeTv.setText(AschSDK.Helper.dateFromAschTimestamp((int) blockInfo.getTimestamp()).toString());
            versionTv.setText(versionInfo.getVersion());
            buildTv.setText(versionInfo.getBuild());
            netTv.setText(versionInfo.getNet());
        }

    }
}
