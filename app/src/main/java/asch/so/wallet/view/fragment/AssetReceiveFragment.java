package asch.so.wallet.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    @BindView(R.id.ammount_et)
    EditText ammountEt;
    String currency;

    TextWatcher textWatcher=new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            presenter.generateQrCode(addressTv.getText().toString().trim(),currency,ammountEt.getText().toString().trim());
        }
    };

    private AssetReceiveContract.Presenter presenter;
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

        ammountEt.addTextChangedListener(textWatcher);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ammountEt.removeTextChangedListener(textWatcher);
    }

    @Override
    public void setPresenter(AssetReceiveContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayQrCode(Bitmap bitmap) {
        qrcodeIv.setImageBitmap(bitmap);
    }
}
