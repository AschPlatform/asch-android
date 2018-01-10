package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.BlockInfoContract;
import asch.so.wallet.model.entity.FullAccount;
import asch.so.wallet.presenter.BlockInfoPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/11/24.
 */

public class BlockInfoFragment extends BaseFragment implements BlockInfoContract.View{
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
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;

    private BlockInfoContract.Presenter presenter;

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

        presenter=new BlockInfoPresenter(getContext(),this);
        presenter.loadBlockInfo();
        loadingLayout.showLoading();
        loadingLayout.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadBlockInfo();
                loadingLayout.showLoading();
            }
        });
        return rootView;
    }

    private void showBlockInfo( FullAccount account){

        if (account!=null){
            FullAccount.BlockInfo blockInfo =account.getLatestBlock();
            FullAccount.VersionInfo versionInfo =account.getVersion();

            blockHeightTv.setText(String.valueOf(blockInfo.getHeight()));
            String dateTime = TimeUtils.date2String(AschSDK.Helper.dateFromAschTimestamp((int) blockInfo.getTimestamp()));
            blockTimeTv.setText(dateTime);
            versionTv.setText(versionInfo.getVersion());
            buildTv.setText(versionInfo.getBuild());
            netTv.setText(versionInfo.getNet());
        }

    }

    @Override
    public void setPresenter(BlockInfoContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        loadingLayout.showError();
        //Toast.makeText(getContext(),exception!=null?exception.getMessage():"网络错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayBlockInfo(FullAccount account) {
        if (account==null){
            loadingLayout.showEmpty();
        }else {
            showBlockInfo(account);
            loadingLayout.showContent();
        }


    }
}
