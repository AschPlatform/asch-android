package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.QRCodeScanActivity;
import asch.so.wallet.contract.AssetTransferContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.QRCodeURL;
import asch.so.wallet.model.entity.UIAAsset;
import asch.so.wallet.presenter.AssetTransferPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import asch.so.wallet.view.widget.SecondPasswdDialog;
import asch.so.wallet.view.widget.TransferConfirmationDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.impl.AschConst;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransferFragment extends BaseFragment implements AssetTransferContract.View{
    private  static  final String TAG=AssetTransferFragment.class.getSimpleName();

    AssetTransferContract.Presenter presenter;

    @BindView(R.id.target_et)
    EditText targetEt;
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

    KProgressHUD hud;
    private Balance balance;
    private QRCodeURL qrCodeURL;
    private SecondPasswdDialog secondPasswdDialog;

    String currency=null;
    int precision = 0;

    public static AssetTransferFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetTransferFragment fragment = new AssetTransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        balance= JSON.parseObject(getArguments().getString("balance"),Balance.class);
        String uri = getArguments().getString("qrcode_uri");
        presenter =new AssetTransferPresenter(getContext(),this);
        try {
            if (Validation.isValidAddress(uri)){
                qrCodeURL=new QRCodeURL();
                qrCodeURL.setAmount("");
                qrCodeURL.setCurrency("XAS");
                qrCodeURL.setAddress(uri);
            }else {
                qrCodeURL=QRCodeURL.decodeQRCodeURL(uri);
            }

        }catch (Exception e){
            //
        }
        setHasOptionsMenu(true);
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    private boolean hasSecondPasswd(){
        return getAccount().hasSecondSecret();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transfer,container,false);
        ButterKnife.bind(this,rootView);
        hideKeyboard();

        if (balance!=null){
            currency= balance.getCurrency(); //"KIM.KIM";
            precision=balance.getPrecision();
        } else if(qrCodeURL!=null) {
            currency= qrCodeURL.getCurrency();
            precision=8;
        }

        targetEt.setKeyListener(DigitsKeyListener.getInstance(AppConstants.DIGITS));
        if (hasSecondPasswd()){
            secondPasswdLl.setVisibility(View.VISIBLE);
        }else {
            secondPasswdLl.setVisibility(View.GONE);
        }

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String targetAddress= targetEt.getText().toString().trim();
                String ammountStr=amountEt.getText().toString().trim();
                //Account account=getAccount();
                String message=memoEt.getText().toString();
               // String secret=account.getSeed();
                String secondSecret=secondPasswdEt.getText().toString().trim();
                //String secondSecret= null; //TestData.secondSecret;

                if (currency==null)
                {
                    AppUtil.toastError(getContext(),"请选择币种");
                    return;
                }

                if (!Validator.check(getContext(), Validator.Type.Address,targetAddress,"无效地址，请重新输入")){
                   return;
                }
                if (!Validator.check(getContext(), Validator.Type.Amount,ammountStr,"无效金额")){
                    return;
                }

                if (hasSecondPasswd()){
                    if (!Validator.check(getContext(),Validator.Type.SecondSecret,secondSecret,"二级密码不正确")){
                        return;
                    }
                }

                showConfirmationDialog(targetAddress, ammountStr, currency,secondSecret, new TransferConfirmationDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(TransferConfirmationDialog dialog) {
                        long amount=(long)(Float.parseFloat(ammountStr)*Math.pow(10,precision));
                        Account currentAccount =AccountsManager.getInstance().getCurrentAccount();
                        if (currentAccount!=null && currentAccount.getFullAccount()!=null && currentAccount.getFullAccount().getAccount().isSecondSignature())
                        {
                            showPasswordInputDialog(new SecondPasswdDialog.PasswordCallback() {
                                @Override
                                public void callback(SecondPasswdDialog dialog, String password) {
                                    if (Validator.check(getContext(), Validator.Type.Password,password,"用户密码不正确"))
                                    {
                                        presenter.transfer(currency,targetAddress,amount,message,null,secondSecret,password);
                                        showHUD();
                                    }
                                }
                            });
                            return;
                        }
                       // presenter.transfer(currency,targetAddress,amount,message,secret,null);
                       // showHUD();
                    }
                });
            }
        });

        feeEt.setKeyListener(null);

        if (qrCodeURL!=null){
            targetEt.setText(qrCodeURL.getAddress());
            amountEt.setText(qrCodeURL.getAmount());
        }

        presenter.loadAssets(currency);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_asset_transfer,menu);
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
        AppUtil.toastError(getContext(),exception!=null?exception.getMessage():"网络错误");
    }

//    @Override
//    public void displayToast(String toast) {
//        Toast.makeText(getActivity(),toast!=null?toast:"转账成功",Toast.LENGTH_SHORT).show();
//        if (secondPasswdDialog!=null){
//            secondPasswdDialog.dismiss();
//        }
//            getActivity().finish();
//    }

//    private boolean hasAsset(String currency){
//       return (getAccount()!=null && getAccount().getFullAccount()!=null && getAccount().getFullAccount().hasAsset(currency));
//    }

    @Override
    public void displayAssets(List<UIAAsset> assets, int selectIndex) {
        Log.d(TAG,"++++assets:"+assets.toString());
        ArrayList<String> nameList=new ArrayList<String>();
        nameList.add(AschConst.CORE_COIN_NAME);
        for (UIAAsset uiaAsset :
                assets) {
            nameList.add(uiaAsset.getName());
        }
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetsSpinner.setAdapter(adapter);
        assetsSpinner.setSelection(selectIndex,true);

    }

    @Override
    public void displayTransferResult(boolean res, String msg) {
        AppUtil.toastSuccess(getContext(),msg);
            scheduleHUDDismiss();
    }

    @Override
    public void displayPasswordValidMessage(boolean res, String msg) {
        if (!res){
            AppUtil.toastError(getContext(),msg);
        }
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


    public void setTargetAddress(String uri){

        try {
            if (uri.startsWith("A")){
                qrCodeURL=new QRCodeURL();
                qrCodeURL.setAmount("0");
                qrCodeURL.setCurrency("XAS");
                qrCodeURL.setAddress(uri);
            }else {
                qrCodeURL=QRCodeURL.decodeQRCodeURL(uri);
            }

        }catch (Exception e){
            //
        }
        if (qrCodeURL!=null){
            targetEt.setText(qrCodeURL.getAddress());
            amountEt.setText(qrCodeURL.getAmount());
            String currency = qrCodeURL.getCurrency();
            if (currency!=null){
                presenter.loadAssets(currency);
            }

        }
    }

}
