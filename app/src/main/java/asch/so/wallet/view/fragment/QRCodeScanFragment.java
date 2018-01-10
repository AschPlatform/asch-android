package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by kimziv on 2017/10/26.
 */

public class QRCodeScanFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.zbarview)
    ZBarView zbarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_qrcode_scan,container,false);
        ButterKnife.bind(this,rootView);
        //zbarView.setDelegate(this);
        return rootView;
    }
}
