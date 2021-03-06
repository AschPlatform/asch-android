package asch.io.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.activity.AssetTransferActivity;
import asch.io.wallet.activity.CheckPasswordActivity;
import asch.io.wallet.activity.QRCodeScanActivity;
import asch.io.wallet.contract.AssetTransferContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.model.entity.QRCodeURL;
import asch.io.wallet.presenter.AssetTransferPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.validator.Validator;
import asch.io.wallet.view.widget.AssetTransferAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.impl.AschConst;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransferFragment extends BaseFragment implements AssetTransferContract.View{
    private  static  final String TAG=AssetTransferFragment.class.getSimpleName();

    AssetTransferContract.Presenter presenter;

    @BindView(R.id.transfer_scroll)
    ScrollView mainSv;
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
    @BindView(R.id.remark_et)
    EditText remarkEt;
    @BindView(R.id.can_use_text)
    TextView canUseTv;
    @BindView(R.id.lock_text)
    TextView lockTv;
    @BindView(R.id.view_lock_info)
    LinearLayout lockLl;
    Unbinder unbinder;


    KProgressHUD hud;
    private AschAsset balanceRemain;
    int precision;
    private QRCodeURL qrCodeURL;
    private String currency=null;
    private AssetTransferActivity.Action action;
    BigDecimal amount;
    String remark;
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

    private AschAsset getBalance(){

        return AssetManager.getInstance().queryAschAssetByName(currency);
    }

    private boolean hasSecondPasswd(){
        return getAccount().hasSecondSecret();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transfer,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        hideKeyboard();

        if (qrCodeURL!=null){
            targetEt.setText(qrCodeURL.getAddress());
            amountEt.setText(qrCodeURL.getAmount());
            String assetName = qrCodeURL.getCurrency();
            this.currency= TextUtils.isEmpty(assetName)? AschConst.CORE_COIN_NAME:assetName;
        }

        this.balanceRemain=getBalance();
        precision=balanceRemain.getPrecision();
        if (balanceRemain.getName().equals(AppConstants.XAS_NAME)){
            lockLl.setVisibility(View.VISIBLE);
            long locked = getAccount().getFullAccount().getAccount().getLockedAmount();
            String lockStr = locked==0?"0":AppUtil.decimalFormat(AppUtil.decimalFromBigint(locked,AppConstants.PRECISION));
            lockTv.setText(lockStr);
            canUseTv.setText(balanceRemain.getBalanceString());
            balanceTv.setText(this.balanceRemain.getXasTotal());
        }else{

            lockLl.setVisibility(View.GONE);
            balanceTv.setText(this.balanceRemain==null?"":this.balanceRemain.getBalanceString());
        }
        if (balanceTv.getText().toString().length()>10)
            balanceTv.setTextSize(16);


        coinNameTv.setText(getBalance().getName());
//        targetEt.setKeyListener(DigitsKeyListener.getInstance(AppConstants.DIGITS));
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
        targetEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainSv==null){
                    return;
                }
                    int y =mainSv.getScrollY();
                    if (y<300)
                        mainSv.smoothScrollTo(0,300);
                }


        });
        amountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mainSv==null) {
                    return;
                }
                    if (hasFocus) {
                        int y = mainSv.getScrollY();
                        if (y < 400)
                            mainSv.smoothScrollTo(0, 400);
                    }
                }


        });
        remarkEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mainSv==null){
                    return;
                }
                if (hasFocus){
                    int y =mainSv.getScrollY();
                    if (y<500)
                        mainSv.smoothScrollTo(0,500);
                }

            }
        });
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String targetAddress= targetEt.getText().toString().trim();
                String amountStr=amountEt.getText().toString().trim();
                remark=remarkEt.getText().toString();
                boolean hasSecondPwd=hasSecondPasswd();

                if (currency==null) {
                    AppUtil.toastError(getContext(),getString(R.string.select_coin));
                    return;
                }

                if (!Validator.check(getContext(),Validator.Type.NickNameOrAddress,targetAddress,getString(R.string.address_invalid))){
                   return;
                }

                if (targetAddress.equals(getAccount().getAddress())){
                    AppUtil.toastError(getContext(),getString(R.string.address_same));
                    return;
                }

                if (!Validator.check(getContext(), Validator.Type.Amount,amountStr,getString(R.string.invalid_money))){
                    return;
                }

                if (!Validator.check(getContext(),Validator.Type.Remark,remark,getString(R.string.remark_err))){
                    return;
                }

                if (targetAddress.equals(getAccount().getFullAccount().getAccount().getName())){
                    AppUtil.toastError(getActivity(),getString(R.string.err_transfer_self));
                    return;
                }

                if (amountStr.contains(".")&&amountStr.substring(amountStr.indexOf(".")+1).length()>precision){
                    AppUtil.toastError(getActivity(),getString(R.string.err_precision));
                    return;
                }

                //余额校验

                amount=new BigDecimal(amountStr);
                BigDecimal remainBd = new BigDecimal(balanceRemain.getBalanceString());
                //XAS转账计算手续费判断，其他的余额可以全转
                if (currency==AppConstants.XAS_NAME){
                    if (remainBd.subtract(new BigDecimal(0.1)).compareTo(amount)!=1){
                        AppUtil.toastError(getContext(),getString(R.string.money_not_enough));
                        return;
                    }
                }else {
                    if (remainBd.compareTo(amount)!=1){
                        AppUtil.toastError(getContext(),getString(R.string.money_not_enough));
                        return;
                    }
                    if (AssetManager.getInstance().queryAschAssetByName(AppConstants.XAS_NAME).getTrueBalance()<(float) 0.1){
                        AppUtil.toastError(getContext(),getString(R.string.not_enough_xas));
                        return;
                    }

                }

                AssetTransferAlertDialog dialog = new AssetTransferAlertDialog(getContext(),targetAddress,
                        amountStr,currency,"").setOnTransferClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
                        Bundle bundle = new Bundle();
                        String title = currency+getString(R.string.transfer);
                        String clazz = AssetTransferFragment.class.getSimpleName();
                        bundle.putString("title",clazz);
                        bundle.putString("currency",currency);
                        bundle.putBoolean("hasSecondPwd",hasSecondPasswd());
                        intent.putExtras(bundle);
                        startActivityForResult(intent,1);
                    }
                });
                dialog.show();


            }
        });

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
            presenter.transfer(currency,targetEt.getText().toString().trim(),AppUtil.scaleStringFromBigAmount(amount.toString(),precision),remark,null,hasSecondPasswd()?secondSecret:null,password);
            showHUD();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
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

//    private void parseQRUri(String uri) {
//      this.parseQRUri(uri,true);
//    }
    private void parseQRUri(String uri){
        try {
            if (Validation.isValidAddress(uri)){
                qrCodeURL=new QRCodeURL();
                qrCodeURL.setAmount("");
                if (this.currency==null)
                    qrCodeURL.setCurrency(AschConst.CORE_COIN_NAME);
                else
                    qrCodeURL.setCurrency(this.currency);
                qrCodeURL.setAddress(uri);
            }else {
                qrCodeURL=QRCodeURL.decodeQRCodeURL(uri);
            }

        }catch (Exception e){
            qrCodeURL=null;
            e.printStackTrace();
        }

    }

    public void setTargetAddress(String uri){
        parseQRUri(uri);
        if (qrCodeURL!=null){
            targetEt.setText(qrCodeURL.getAddress());
            amountEt.setText(qrCodeURL.getAmount());
            String assetName = qrCodeURL.getCurrency();

                //this.currency= TextUtils.isEmpty(assetName)? AschConst.CORE_COIN_NAME:assetName;
                this.currency= assetName;


        }else {
            AppUtil.toastError(getContext(),getString(R.string.receipt_rq_error));
        }

    }

}
