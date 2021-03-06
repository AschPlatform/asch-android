package asch.io.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
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
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.activity.AssetWithdrawActivity;
import asch.io.wallet.activity.CheckPasswordActivity;
import asch.io.wallet.activity.QRCodeScanActivity;
import asch.io.wallet.contract.AssetWithdrawContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.model.entity.QRCodeURL;
import asch.io.wallet.presenter.AssetWithdrawPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.impl.AschConst;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetWithdrawFragment extends BaseFragment implements AssetWithdrawContract.View{
    private  static  final String TAG=AssetWithdrawFragment.class.getSimpleName();

    AssetWithdrawContract.Presenter presenter;
    @BindView(R.id.withdraw_sv)
    ScrollView mainSv;
    @BindView(R.id.withdraw_fee)
    TextView feeTv;
    @BindView(R.id.withdraw_seekerBar)
    AppCompatSeekBar feeSeekBar;
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
    private AschAsset balanceRemain;
    private QRCodeURL qrCodeURL;
    private HashMap<String, AschAsset> assetsMap;
    private List<String> nameList;
    private AschAsset selectedAsset;
    private String currency;
    private AssetWithdrawActivity.Action action;
    long amount;
    private int max = 10000;
    private double fee = 0.00001;
    private long longFee;
    private String minFee = "0.00001";
    private String gateway = null;
    Unbinder unbinder;
    public static AssetWithdrawFragment newInstance() {
        Bundle args = new Bundle();
        AssetWithdrawFragment fragment = new AssetWithdrawFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action= AssetWithdrawActivity.Action.valueOf(getArguments().getInt("action"));
        String uri = getArguments().getString("qrcode_uri");
        parseQRUri(uri);
        presenter =new AssetWithdrawPresenter(getContext(),this);
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }



    private boolean hasSecondPasswd(){
        return getAccount().hasSecondSecret();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_withdraw,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        if (qrCodeURL!=null){
//            targetEt.setText(qrCodeURL.getAddress());
//            amountEt.setText(qrCodeURL.getAmount());
            String assetName = qrCodeURL.getCurrency();
            this.currency= TextUtils.isEmpty(assetName)? AschConst.COIN_NAME_BCH:assetName;
        }


        this.balanceRemain=AssetManager.getInstance().queryAschAssetByName(currency);

        balanceTv.setText(this.balanceRemain==null?"":this.balanceRemain.getBalanceString());
        feeTv.setText(minFee + " " + balanceRemain.getName());
        feeSeekBar.setMax(max);
        feeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress==0){
                    progress =1;
                }
                fee = Double.valueOf(minFee) * progress;
                String v = String.format("%.5f", fee);
                feeTv.setText(v + " " + balanceRemain.getName());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        coinNameTv.setText(balanceRemain.getName());
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), QRCodeScanActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("action", QRCodeScanActivity.Action.ScanBCHAddressToPaste.value);
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
                int y = mainSv.getScrollY();
                if (y < 200)
                    mainSv.smoothScrollTo(0, 200);

            }
        });
        amountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mainSv==null){
                    return;
                }
                    int y =mainSv.getScrollY();
                    if (y<300)
                        mainSv.smoothScrollTo(0,300);
                }

        });
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String targetAddress= targetEt.getText().toString().trim();
                String amountStr=amountEt.getText().toString().trim();
                String message=memoEt.getText().toString();
                boolean hasSecondPwd=hasSecondPasswd();

                if (TextUtils.isEmpty(balanceRemain.getName())) {
                    AppUtil.toastError(getContext(),getString(R.string.err_get_asset));
                    return;
                }

                if (!Validator.check(getContext(), Validator.Type.BchAddress,targetAddress,getString(R.string.address_invalid))){
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
                int precision=balanceRemain.getPrecision();
                amount = strToLong(amountStr,precision);
                long remainBalance = balanceRemain!=null?balanceRemain.getLongBalance():-1;
                if (remainBalance>=0 && remainBalance<amount-fee){
                    AppUtil.toastError(getContext(),getString(R.string.money_not_enough));
                    return;
                }

                gateway = balanceRemain.getGateway();

                longFee = strToLong(String.valueOf(fee),precision);

                if (longFee>amount){
                    AppUtil.toastError(getActivity(),getString(R.string.amount_bigger_fee));
                    return;
                }

                Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
                Bundle bundle = new Bundle();
                String clazz = AssetWithdrawFragment.class.getSimpleName();
                bundle.putString("title",clazz);
                bundle.putString("currency",currency);
                bundle.putBoolean("hasSecondPwd",hasSecondPwd);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);

            }
        });

        hideKeyboard();
        return rootView;
    }


    private long strToLong(String amount,int precision){
        BigDecimal amountDecimal=new BigDecimal(amount);
        MathContext mc=new MathContext(amount.length(), RoundingMode.HALF_UP);
        amountDecimal=amountDecimal.multiply(new BigDecimal(10).pow(precision),mc);
        return amountDecimal.longValue();
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

            presenter.withdraw(currency,gateway,String.valueOf(longFee),
                    targetEt.getText().toString().trim(),amount,"",null,hasSecondPasswd()?secondSecret:null,password);

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
                bundle.putInt("action", QRCodeScanActivity.Action.ScanBCHAddressToPaste.value);
                intent.putExtras(bundle);
                startActivityForResult(intent, 11);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setPresenter(AssetWithdrawContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(Throwable exception) {
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

    private void parseQRUri(String uri){
        try {
            qrCodeURL=QRCodeURL.decodeQRCodeURL(uri);
        }catch (Exception e){
            qrCodeURL=null;
            e.printStackTrace();
        }
    }

    public void setTargetAddress(String uri){
        parseQRUri(uri);

        if (qrCodeURL==null){
            AppUtil.toastError(getContext(),getString(R.string.receipt_rq_error));
        }

        if (qrCodeURL.getAddress()!=null)
            targetEt.setText(qrCodeURL.getAddress());

        if (qrCodeURL.getAmount()!=null)
            amountEt.setText(qrCodeURL.getAmount());

    }
}
