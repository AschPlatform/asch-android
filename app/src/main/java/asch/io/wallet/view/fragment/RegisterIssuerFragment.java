package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.activity.CheckPasswordActivity;
import asch.io.wallet.activity.IssuerAssetsActivity;
import asch.io.wallet.contract.RegisterIssuerContract;
import asch.io.wallet.presenter.RegisterIssuerPresenter;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import so.asch.sdk.impl.Validation;


public class RegisterIssuerFragment extends BaseFragment implements RegisterIssuerContract.View {
    @BindView(R.id.issuer_name_et)
    EditText issuerNameEt;
    @BindView(R.id.issuer_name_count)
    TextView issuerNameCount;
    @BindView(R.id.issuer_detail_et)
    EditText issuerDetailEt;
    @BindView(R.id.issuer_detail_count)
    TextView issuerDetailCount;
    Unbinder unbinder;
    RegisterIssuerContract.Presenter presenter;
    KProgressHUD hud;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =inflater.inflate(R.layout.fragment_register_issuer,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        issuerNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int c = s.toString().length();
                String amount = "/15";
                String current = String.valueOf(c);
                if (c>15){
                    issuerNameCount.setTextColor(Color.RED);
                }else
                    issuerNameCount.setTextColor(Color.BLACK);

                issuerNameCount.setText(current+amount);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        issuerDetailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int c = s.toString().length();
                String amount = "/255";
                String current = String.valueOf(c);
                if (c>255){
                    issuerDetailCount.setTextColor(Color.RED);
                }else
                    issuerDetailCount.setTextColor(Color.BLACK);

                issuerDetailCount.setText(current+amount);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        presenter = new RegisterIssuerPresenter(getContext(),this);
        return rootView;
    }

    @OnClick(R.id.register_issuer_btn) void onRegisterIssuerBtnClick() {

        String name = issuerNameEt.getText().toString();
        String desc = issuerDetailEt.getText().toString();

        if (TextUtils.isEmpty(name)){
            AppUtil.toastError(getContext(),getString(R.string.err_issuer_name_empty));
            return;
        }
        if (!Validation.isValidIssueName(name)){
            AppUtil.toastError(getContext(),getString(R.string.err_issuer_name_format));
            return;
        }
        if (TextUtils.isEmpty(desc)){
            AppUtil.toastError(getContext(),getString(R.string.err_issuer_desc_empty));
            return;
        }
        if (desc.length()>255||name.length()>15){
            AppUtil.toastError(getContext(),getString(R.string.err_length));
            return;
        }
        if (AssetManager.getInstance().queryAschAssetByName(AppConstants.XAS_NAME).getTrueBalance()<(float) 100.0){
            AppUtil.toastError(getContext(),getString(R.string.error_balance_insufficient));
            return;
        }

        Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
        Bundle bundle = new Bundle();
        String clazz = RegisterIssuerFragment.class.getSimpleName();
        bundle.putString("title",clazz);
        bundle.putBoolean("hasSecondPwd",hasSecondPwd());
        intent.putExtras(bundle);
        startActivityForResult(intent,1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==1){
            String secondSecret = data.getStringExtra("secondPwd");
            String password = data.getStringExtra("password");

            if (hasSecondPwd()&&TextUtils.isEmpty(secondSecret)){
                AppUtil.toastError(getContext(),getString(R.string.error_failed_to_verify_signature));
                return;
            }

            if (TextUtils.isEmpty(password)){
                AppUtil.toastError(getContext(),getString(R.string.error_failed_to_verify_signature));
                return;
            }
            presenter.register(issuerNameEt.getText().toString(),issuerDetailEt.getText().toString(),password,secondSecret);
            showHUD();
        }
    }

    private boolean hasSecondPwd(){
        return AccountsManager.getInstance().getCurrentAccount().hasSecondSecret();
    }

    public static RegisterIssuerFragment newInstance() {

        Bundle args = new Bundle();
        RegisterIssuerFragment fragment = new RegisterIssuerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void displaySuccess() {
        dismissHUD();
        AppUtil.toastSuccess(getActivity(),getString(R.string.register_issuer_success));
        Intent intent = new Intent(getActivity(),IssuerAssetsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void setPresenter(RegisterIssuerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayError(Throwable exception) {
        AppUtil.toastError(getContext(),exception.getMessage().toString());
        dismissHUD();
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
}
