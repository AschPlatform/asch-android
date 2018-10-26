package asch.so.wallet.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.AssetManager;
import asch.so.wallet.contract.DAppDepositContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.AschAsset;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.QRCodeURL;
import asch.so.wallet.presenter.DAppDepositPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import asch.so.wallet.view.widget.SecondPasswdDialog;
import asch.so.wallet.view.widget.TransferConfirmationDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2018/2/11.
 */

public class DAppDepositFragment extends BaseFragment implements DAppDepositContract.View{

    private  static  final String TAG=AssetTransferFragment.class.getSimpleName();

    DAppDepositContract.Presenter presenter;

    @BindView(R.id.ammount_et)
    EditText amountEt;
    @BindView(R.id.memo_et)
    EditText memoEt;
    @BindView(R.id.fee_et)
    EditText feeEt;
    @BindView(R.id.transfer_btn)
    Button transferBtn;
    @BindView(R.id.assets_sp)
    Spinner assetsSpinner;
    @BindView(R.id.second_passwd_ll)
    View secondPasswdLl;
    @BindView(R.id.second_passwd_et)
    EditText secondPasswdEt;
    @BindView(R.id.balance_tv)
    TextView balanceTv;
    Unbinder unbinder;

    KProgressHUD hud;
    private Balance balance;
    private QRCodeURL qrCodeURL;
    private SecondPasswdDialog secondPasswdDialog;
    private BaseAsset selectedAsset;
    private String currency=null;
    private String dappId=null;

    public static DAppDepositFragment newInstance() {

        Bundle args = new Bundle();

        DAppDepositFragment fragment = new DAppDepositFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        balance= JSON.parseObject(getArguments().getString("balance"),Balance.class);
        dappId=getArguments().getString("dapp_id");
        presenter =new DAppDepositPresenter(getContext(),this);
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    private AschAsset getBalance(){
        return AssetManager.getInstance().queryAschAssetByName(currency);
    }

    private boolean hasSecondPasswd(){
        return getAccount().hasSecondSecret();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_dapp_deposit,container,false);
        unbinder = ButterKnife.bind(this, rootView);

        hideKeyboard();

//        if (qrCodeURL!=null){
//            targetEt.setText(qrCodeURL.getAddress());
//            amountEt.setText(qrCodeURL.getAmount());
//            String assetName = qrCodeURL.getCurrency();
//            this.currency= TextUtils.isEmpty(assetName)? AschConst.CORE_COIN_NAME:assetName;
//        }else {
//            // AppUtil.toastError(getContext(),"收款二维码有错误");
//        }

        AschAsset balanceRemain=getBalance();
        balanceTv.setText(balanceRemain==null?"":balanceRemain.getBalanceString());

        //targetEt.setKeyListener(DigitsKeyListener.getInstance(AppConstants.DIGITS));
        if (hasSecondPasswd()){
            secondPasswdLl.setVisibility(View.VISIBLE);
        }else {
            secondPasswdLl.setVisibility(View.GONE);
        }

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String targetAddress= targetEt.getText().toString().trim();
                String ammountStr=amountEt.getText().toString().trim();
                String message=memoEt.getText().toString();
                String secondSecret=secondPasswdEt.getText().toString().trim();
                boolean hasSecondPwd=hasSecondPasswd();

                if (currency==null)
                {
                    AppUtil.toastError(getContext(),getString(R.string.select_coin));
                    return;
                }

//                if (!Validator.check(getContext(), Validator.Type.Address,targetAddress,getString(R.string.address_invalid))){
//                    return;
//                }

//                if (targetAddress.equals(getAccount().getAddress())){
//                    AppUtil.toastError(getContext(),getString(R.string.address_same));
//                    return;
//                }

                if (!Validator.check(getContext(), Validator.Type.Amount,ammountStr,getString(R.string.invalid_money))){
                    return;
                }
                int precision=selectedAsset.getPrecision();
                BigDecimal amountDecimal=new BigDecimal(ammountStr);
                MathContext mc=new MathContext(ammountStr.length(), RoundingMode.HALF_UP);
                amountDecimal=amountDecimal.multiply(new BigDecimal(10).pow(precision),mc);
                long amount = amountDecimal.longValue();
                long remainBalance=balanceRemain!=null?balanceRemain.getLongBalance():-1;
                if (remainBalance>=0 && remainBalance<amount){
                    AppUtil.toastError(getContext(),getString(R.string.money_not_enough));
                    return;
                }

                if (hasSecondPwd){
                    if (!Validator.check(getContext(),Validator.Type.SecondSecret,secondSecret,getString(R.string.secondary_password_error))){
                        return;
                    }
                }

                showConfirmationDialog(dappId, ammountStr, currency,secondSecret, new TransferConfirmationDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(TransferConfirmationDialog dialog) {
                        Account currentAccount =AccountsManager.getInstance().getCurrentAccount();
                        if (currentAccount!=null)
                        {
                            showPasswordInputDialog(new SecondPasswdDialog.PasswordCallback() {
                                @Override
                                public void callback(SecondPasswdDialog dialog, String password) {
                                    if (Validator.check(getContext(), Validator.Type.Password,password,getString(R.string.account_password_error)))
                                    {
                                        presenter.transfer(dappId,currency,amount,message,null,hasSecondPwd?secondSecret:null,password);
                                        showHUD();
                                    }
                                }
                            });
                            return;
                        }
                    }
                });
            }
        });
        amountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                balanceTv.setVisibility(hasFocus?View.VISIBLE:View.INVISIBLE);
            }
        });
        feeEt.setKeyListener(null);
        presenter.loadAssets(currency,false);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }

    private void showConfirmationDialog(String address, String amount, String currency, String secondPasswd, TransferConfirmationDialog.OnConfirmListener onConfirmListener){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        TransferConfirmationDialog dialog =TransferConfirmationDialog.newInstance(address,amount,currency,secondPasswd);
        dialog.setOnClickListener(onConfirmListener);
        dialog.show(fm,"confirmation");
    }

    private void showPasswordInputDialog(SecondPasswdDialog.PasswordCallback callback){
        secondPasswdDialog = new SecondPasswdDialog(getActivity());
        secondPasswdDialog.setPasswordCallback(callback);
        secondPasswdDialog.clearPasswordText();
        secondPasswdDialog.show();
    }


    private void hideKeyboard(){
        InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),0);
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_asset_transfer,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.item_scan_qrcode:
//            {
//                Intent intent =new Intent(getActivity(), QRCodeScanActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putInt("action", QRCodeScanActivity.Action.ScanAddressToPaste.value);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 11);
//            }
//            break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void setPresenter(DAppDepositContract.Presenter presenter) {
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
        List<String> nameList=new ArrayList<>(assetsMap.keySet());
        //int selectIndex= nameList.indexOf(currency);
        BaseAsset asset=assetsMap.get(currency);
        selectedAsset=asset;
        if (asset==null){
            presenter.loadAssets(currency,true);
        }

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetsSpinner.setAdapter(adapter);
       // assetsSpinner.setSelection(selectIndex,true);
        assetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency=adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                if (secondPasswdDialog!=null){
                    secondPasswdDialog.dismiss();
                }
                dismissHUD();
                getActivity().finish();
            }
        }, 200);
    }
}
