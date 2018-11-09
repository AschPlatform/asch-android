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

import java.util.LinkedHashMap;

import asch.io.wallet.R;
import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.AssetReceiveContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.BaseAsset;
import asch.io.wallet.presenter.AssetReceivePresenter;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
    @BindView(R.id.center_ll)
    View centerLl;
    @BindView(R.id.receive_info)
    TextView infoTv;
    Unbinder unbinder;
    private Bitmap qrcodeBmp;

    String currency=AschConst.CORE_COIN_NAME;
    private Account account;
   // private HashMap<String, BaseAsset> assetsMap;

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
        Bundle args = getArguments();
        String cur =args.getString("currency");
        currency=cur!=null?cur:AschConst.CORE_COIN_NAME;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_asset_receive,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        infoTv.setText(R.string.receive_info);

//            infoTv.setText(String.format(getString(R.string.receive_mulit_chain).toString(),currency,currency));

        presenter=new AssetReceivePresenter(getActivity(),this);
        //fragment.setPresenter(presenter);

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
                qrcodeBmp  = ImageUtils.view2Bitmap(centerLl);
                if (qrcodeBmp!=null) {
                    presenter.saveQrCode(qrcodeBmp);
                }
            }
        });

        presenter.generateQrCode(account.getAddress());
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
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void setPresenter(AssetReceiveContract.Presenter presenter) {
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


    @Override
    public void displayAssets(LinkedHashMap<String,BaseAsset> assetsMap) {
//        LogUtils.dTag(TAG,"++++assets:"+assetsMap.toString());
//        List<String> nameList=new ArrayList<>(assetsMap.keySet());
//        int selectIndex= nameList.indexOf(currency);
//        selectIndex=selectIndex==-1?0:selectIndex;
//        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nameList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        assetsSp.setAdapter(adapter);
//        assetsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                //currency = nameList.get(position);
//                currency = adapter.getItem(position);
//                presenter.generateQrCode(addressTv.getText().toString().trim(),currency,ammountEt.getText().toString().trim());
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        assetsSp.setSelection(selectIndex,true);
    }
}
