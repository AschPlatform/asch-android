package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import asch.so.wallet.R;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.contract.AssetReceiveContract;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetReceiveFragment extends BaseFragment implements AssetReceiveContract.View{

    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.qrcode_iv)
    ImageView qrcodeIv;
    @BindView(R.id.copy_btn)
    Button copyBtn;

    public static AssetReceiveFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetReceiveFragment fragment = new AssetReceiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_asset_receive,container,false);
        ButterKnife.bind(this,rootView);

        return rootView;
    }

    @Override
    public void setPresenter(AssetReceiveContract.Presenter presenter) {

    }

    @Override
    public void displayQrCode() {

    }
}
