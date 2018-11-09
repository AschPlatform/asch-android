package asch.io.wallet.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.contract.AssetGatewayDepositContract;
import asch.io.wallet.presenter.AssetGatewayDepositPresenter;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetGatewayDepositFragment extends BaseFragment implements AssetGatewayDepositContract.View{
    private  static  final String TAG=AssetGatewayDepositFragment.class.getSimpleName();
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.qrcode_iv)
    ImageView qrcodeIv;
    @BindView(R.id.copy_btn)
    Button copyBtn;

    @BindView(R.id.ammount_et)
    EditText ammountEt;
    @BindView(R.id.save_btn)
     TextView saveTv;
    @BindView(R.id.assets_sp)
    Spinner assetsSp;
    @BindView(R.id.center_ll)
    View centerLl;
    @BindView(R.id.receive_info)
    TextView infoTv;

    private Bitmap qrcodeBmp;

    String currency="";
    String address = "";
    Unbinder unbinder;
    TextWatcher textWatcher=new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            presenter.generateQrCode(addressTv.getText().toString());
        }
    };

    private AssetGatewayDepositContract.Presenter presenter;
    public static AssetGatewayDepositFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetGatewayDepositFragment fragment = new AssetGatewayDepositFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        String cur =args.getString("currency");
        currency=cur;
        address = args.getString("address");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_asset_receive,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        infoTv.setText(String.format(getString(R.string.receive_multi_chain),currency,currency));

//            infoTv.setText(String.format(getString(R.string.receive_mulit_chain).toString(),currency,currency));

        presenter=new AssetGatewayDepositPresenter(getActivity(),this);
        //fragment.setPresenter(presenter);

        addressTv.setText(address);
        ammountEt.addTextChangedListener(textWatcher);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                copyAddress(address);
            }
        });
        
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrcodeBmp  = ImageUtils.view2Bitmap(centerLl);
                if (qrcodeBmp!=null) {
                    presenter.saveQrCode(qrcodeBmp);
                }
            }
        });

        presenter.generateQrCode(address);
        presenter.loadAssets(false);
        return rootView;
    }

    private void copyAddress(String content){
        AppUtil.copyText(getActivity(), content);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (qrcodeBmp!=null){
            qrcodeBmp.recycle();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ammountEt.removeTextChangedListener(textWatcher);
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void setPresenter(AssetGatewayDepositContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        if (getContext()!=null){
            AppUtil.toastError(getContext(),exception.getMessage());
        }

    }

    @Override
    public void displayQrCode(Bitmap bitmap) {
        qrcodeIv.setImageBitmap(bitmap);
       // qrcodeBmp=bitmap;
    }

}
