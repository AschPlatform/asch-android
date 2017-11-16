package asch.so.wallet.view.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import asch.so.base.view.UIException;
import asch.so.wallet.R;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AssetReceiveContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.QRCodeURL;
import asch.so.wallet.model.entity.UIAAsset;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetReceiveFragment extends BaseFragment implements AssetReceiveContract.View{
    private  static  final String TAG=AssetReceiveFragment.class.getSimpleName();
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
    private Bitmap qrcodeBmp;

    String currency="XAS";
    private Account account;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account= AccountsManager.getInstance().getCurrentAccount();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_asset_receive,container,false);
        ButterKnife.bind(this,rootView);
        addressTv.setText(account.getAddress());
        ammountEt.addTextChangedListener(textWatcher);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                copyAddress(account.getAddress());
            }
        });
        
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qrcodeBmp!=null) {
                    presenter.saveQrCode(qrcodeBmp);
                }
            }
        });

        presenter.generateQrCode(account.getAddress(),"XAS","8");
        presenter.loadAssets();
        return rootView;
    }

    private void copyAddress(String content){
       ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, content);
        clipboardManager.setText(content);
        //clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getContext(),clipboardManager.getText(),Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void setPresenter(AssetReceiveContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(UIException exception) {
        Toast.makeText(getContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayQrCode(Bitmap bitmap) {
        qrcodeIv.setImageBitmap(bitmap);
        qrcodeBmp=bitmap;
    }


    @Override
    public void displayAssets(List<UIAAsset> assets) {
        Log.d(TAG,"++++assets:"+assets.toString());
        ArrayList<String> nameList=new ArrayList<String>();
        nameList.add(AschConst.CORE_COIN_NAME);
        assets.forEach(new Consumer<UIAAsset>() {
            @Override
            public void accept(UIAAsset uiaAsset) {
                nameList.add(uiaAsset.getName());
            }
        });
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetsSp.setAdapter(adapter);
        assetsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //currency = nameList.get(position);
                currency = adapter.getItem(position);
                presenter.generateQrCode(addressTv.getText().toString().trim(),currency,ammountEt.getText().toString().trim());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
