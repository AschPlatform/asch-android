package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.activity.CheckPasswordActivity;
import asch.so.wallet.activity.QRCodeScanActivity;
import asch.so.wallet.contract.AssetTransferContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.QRCodeURL;
import asch.so.wallet.presenter.AssetTransferPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.impl.AschConst;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransferFragment extends BaseFragment implements AssetTransferContract.View, AdapterView.OnItemSelectedListener{
    private  static  final String TAG=AssetTransferFragment.class.getSimpleName();

    AssetTransferContract.Presenter presenter;

    @BindView(R.id.target_et)
    EditText targetEt;
    @BindView(R.id.ammount_et)
    EditText amountEt;
    @BindView(R.id.transfer_btn)
    Button transferBtn;
    @BindView(R.id.transfer_balance)
    TextView balanceTv;
    @BindView(R.id.transfer_scan)
    ImageView scan;
    @BindView(R.id.transfer_coin_name)
    TextView coinNameTv;

    //暂时隐藏
    @BindView(R.id.memo_et)
    EditText memoEt;

    KProgressHUD hud;
    private Balance balanceRemain;
    private QRCodeURL qrCodeURL;
    private HashMap<String, BaseAsset> assetsMap;
    private List<String> nameList;
    private BaseAsset selectedAsset;
    private String currency=null;
    private AssetTransferActivity.Action action;
    long amount;

    public static AssetTransferFragment newInstance() {
        
        Bundle args = new Bundle();
        AssetTransferFragment fragment = new AssetTransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action= AssetTransferActivity.Action.valueOf(getArguments().getInt("action"));
        String uri = getArguments().getString("qrcode_uri");
        parseQRUri(uri);
        presenter =new AssetTransferPresenter(getContext(),this);
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    private Balance getBalance(){
        if (currency!=null && (getAccount().getFullAccount()!=null)&&(getAccount().getFullAccount().getBalancesMap()!=null)){
            return  getAccount().getFullAccount().getBalancesMap().get(currency);
        }
        return null;
    }

    private boolean hasSecondPasswd(){
        return getAccount().hasSecondSecret();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transfer,container,false);
        ButterKnife.bind(this,rootView);
        hideKeyboard();

        if (qrCodeURL!=null){
            targetEt.setText(qrCodeURL.getAddress());
            amountEt.setText(qrCodeURL.getAmount());
            String assetName = qrCodeURL.getCurrency();
            this.currency= TextUtils.isEmpty(assetName)? AschConst.CORE_COIN_NAME:assetName;
        }else {
            // AppUtil.toastError(getContext(),"收款二维码有错误");
        }

        this.balanceRemain=getBalance();
        balanceTv.setText(this.balanceRemain==null?"":this.balanceRemain.getBalanceString());
        coinNameTv.setText(getBalance().getCurrency());
        targetEt.setKeyListener(DigitsKeyListener.getInstance(AppConstants.DIGITS));
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), QRCodeScanActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("action", QRCodeScanActivity.Action.ScanAddressToPaste.value);
                intent.putExtras(bundle);
                startActivityForResult(intent, 11);
            }
        });

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String targetAddress= targetEt.getText().toString().trim();
                String amountStr=amountEt.getText().toString().trim();
                String message=memoEt.getText().toString();
                boolean hasSecondPwd=hasSecondPasswd();

                if (currency==null) {
                    AppUtil.toastError(getContext(),getString(R.string.select_coin));
                    return;
                }

                if (!Validator.check(getContext(), Validator.Type.Address,targetAddress,getString(R.string.address_invalid))){
                   return;
                }

                if (targetAddress.equals(getAccount().getAddress())){
                    AppUtil.toastError(getContext(),getString(R.string.address_same));
                    return;
                }

                if (!Validator.check(getContext(), Validator.Type.Amount,amountStr,getString(R.string.invalid_money))){
                    return;
                }

                //余额校验
                int precision=selectedAsset.getPrecision();
                BigDecimal amountDecimal=new BigDecimal(amountStr);
                MathContext mc=new MathContext(amountStr.length(), RoundingMode.HALF_UP);
                amountDecimal=amountDecimal.multiply(new BigDecimal(10).pow(precision),mc);
                amount = amountDecimal.longValue();
                long remainBalance=balanceRemain!=null?balanceRemain.getLongBalance():-1;
                if (remainBalance>=0 && remainBalance-0.1<amount){
                    AppUtil.toastError(getContext(),getString(R.string.money_not_enough));
                    return;
                }

                Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
                Bundle bundle = new Bundle();
                String title = currency+getString(R.string.transfer);
                String clazz = AssetTransferFragment.class.getSimpleName();
                bundle.putString("title",clazz);
                bundle.putString("currency",currency);
                bundle.putBoolean("hasSecondPwd",hasSecondPwd);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);

            }
        });


        presenter.loadAssets(currency,false);
        return rootView;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==1){
            String secondSecret = data.getStringExtra("secondPwd");
            String password = data.getStringExtra("password");

            if (hasSecondPasswd()&&TextUtils.isEmpty(secondSecret)){
                AppUtil.toastError(getContext(),getString(R.string.error_failed_to_verify_signature));
                return;
            }

            if (TextUtils.isEmpty(password)){
                AppUtil.toastError(getContext(),getString(R.string.error_failed_to_verify_signature));
                return;
            }

            presenter.transfer(currency,targetEt.getText().toString().trim(),amount,"",null,hasSecondPasswd()?secondSecret:null,password);
            showHUD();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
    }


    private void hideKeyboard(){
        InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),0);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_asset_transfer,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_scan_qrcode:
            {
                Intent intent =new Intent(getActivity(), QRCodeScanActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("action", QRCodeScanActivity.Action.ScanAddressToPaste.value);
                intent.putExtras(bundle);
                startActivityForResult(intent, 11);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(AssetTransferContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        dismissHUD();
        AppUtil.toastError(getContext(),exception!=null?exception.getMessage():getString(R.string.net_error));
    }


    @Override
   public void displayAssets(LinkedHashMap<String,BaseAsset> assetsMap){
        LogUtils.dTag(TAG,"++++assets:"+assetsMap.toString());
        this.assetsMap=assetsMap;
        List<String> nameList=new ArrayList<>(assetsMap.keySet());
        this.nameList=nameList;
        int selectIndex= nameList.indexOf(currency);
        BaseAsset asset=assetsMap.get(currency);
        selectedAsset=asset;
        if (asset==null){
            presenter.loadAssets(currency,true);
        }

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (this.assetsMap!=null && this.nameList!=null){
            this.currency=this.nameList.get(position);
            //int selectIndex= nameList.indexOf(currency);
            this.selectedAsset=assetsMap.get(currency);
            this.balanceRemain=getBalance();

            balanceTv.setText(this.balanceRemain==null?"":this.balanceRemain.getBalanceString());
        }
    }


    @Override
    public void displayTransferResult(boolean res, String msg) {
        AppUtil.toastSuccess(getContext(),msg);
            scheduleHUDDismiss();
    }

    @Override
    public void displayPasswordValidMessage(boolean res, String msg) {
        //if (!res){
        dismissHUD();
        AppUtil.toastError(getContext(),msg);
        //}

    }

    private  void  showHUD(){
        if (hud==null){
            hud = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .show();
        }
    }
    private  void  dismissHUD(){
        if (hud!=null) {
            hud.dismiss();
            hud=null;
        }
    }

    private void scheduleHUDDismiss() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dismissHUD();
                getActivity().finish();
            }
        }, 200);
    }

    private void parseQRUri(String uri){
        try {
            if (Validation.isValidAddress(uri)){
                qrCodeURL=new QRCodeURL();
                qrCodeURL.setAmount("");
                qrCodeURL.setCurrency(AschConst.CORE_COIN_NAME);
                qrCodeURL.setAddress(uri);
            }else {
                qrCodeURL=QRCodeURL.decodeQRCodeURL(uri);
            }

        }catch (Exception e){
            qrCodeURL=null;
            e.printStackTrace();
        }

//        if (qrCodeURL!=null){
//            targetEt.setText(qrCodeURL.getAddress());
//            amountEt.setText(qrCodeURL.getAmount());
//            String assetName = qrCodeURL.getCurrency();
//            this.currency= TextUtils.isEmpty(assetName)? AschConst.CORE_COIN_NAME:assetName;
//        }else {
//            AppUtil.toastError(getContext(),"收款二维码有错误");
//        }
    }

    public void setTargetAddress(String uri){
        parseQRUri(uri);
//        if (qrCodeURL!=null && qrCodeURL.getAddress()!=null){
//            targetEt.setText(qrCodeURL.getAddress());
//           // amountEt.setText(qrCodeURL.getAmount());
//           // String assetName = qrCodeURL.getCurrency();
////            if (TextUtils.isEmpty(assetName)){
////                presenter.loadAssets(currency,true);
////            }else {
////                //this.currency= TextUtils.isEmpty(assetName)? AschConst.CORE_COIN_NAME:assetName;
////                this.currency= assetName;
////            }
//
//        }else {
//            AppUtil.toastError(getContext(),"收款地址二维码有错误");
//        }
        if (qrCodeURL!=null){
            targetEt.setText(qrCodeURL.getAddress());
            amountEt.setText(qrCodeURL.getAmount());
            String assetName = qrCodeURL.getCurrency();
            if (TextUtils.isEmpty(assetName)){
                presenter.loadAssets(currency,true);
            }else {
                //this.currency= TextUtils.isEmpty(assetName)? AschConst.CORE_COIN_NAME:assetName;
                this.currency= assetName;
            }

        }else {
            AppUtil.toastError(getContext(),getString(R.string.receipt_rq_error));
        }
        presenter.loadAssets(currency,false);
    }

}
