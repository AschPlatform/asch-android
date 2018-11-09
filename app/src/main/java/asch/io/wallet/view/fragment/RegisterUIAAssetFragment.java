package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import asch.io.wallet.contract.RegisterAssetContract;
import asch.io.wallet.presenter.RegisterAssetPresenter;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import so.asch.sdk.impl.Validation;


public class RegisterUIAAssetFragment  extends BaseFragment implements RegisterAssetContract.View {

    @BindView(R.id.issuer_name_et)
    EditText issuerNameEt;
    @BindView(R.id.asset_max)
    EditText assetMax;
    @BindView(R.id.issue_asset_percision)
    EditText issueAssetPrecision;
    @BindView(R.id.issuer_detail_et)
    EditText issuerDetailEt;
    @BindView(R.id.issuer_detail_count)
    TextView issuerDetailCount;
    KProgressHUD hud;

    Unbinder unbinder;
    RegisterAssetContract.Presenter presenter;
    @OnClick(R.id.register_asset) void onRegisterAssetClick() {
        String name = issuerNameEt.getText().toString();
        String max = assetMax.getText().toString();
        String precision = issueAssetPrecision.getText().toString();
        String desc = issuerDetailEt.getText().toString();
        if (!Validation.isValidIssueAssetName(name)){
            AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_name));
            return;
        }

        if (TextUtils.isEmpty(desc)){
            AppUtil.toastError(getActivity(),getString(R.string.err_issuer_desc_empty));
            return;
        }

        if (desc.length()>500){
            AppUtil.toastError(getActivity(),getString(R.string.err_length));
            return;
        }

        if (!Validation.isValidIssueMaximum(max)){
            AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_max));
            return;
        }

        if (!Validation.isValidPrecision(precision)){
            AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_precision));
            return;
        }


        if (AssetManager.getInstance().queryAschAssetByName(AppConstants.XAS_NAME).getTrueBalance()<(float) 500.0){
            AppUtil.toastError(getContext(),getString(R.string.error_balance_insufficient));
            return;
        }

        Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
        Bundle bundle = new Bundle();
        String clazz = RegisterUIAAssetFragment.class.getSimpleName();
        bundle.putString("title",clazz);
        bundle.putBoolean("hasSecondPwd",hasSecondPwd());
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }



    public static RegisterUIAAssetFragment newInstance() {
        Bundle args = new Bundle();
        RegisterUIAAssetFragment fragment = new RegisterUIAAssetFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_register_uiaasset,container,false);
        unbinder= ButterKnife.bind(this,rootView);

        issuerDetailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int c = s.toString().length();
                String amount = "/500";
                String current = String.valueOf(c);
                if (c>500){
                    issuerDetailCount.setTextColor(Color.RED);
                }else
                    issuerDetailCount.setTextColor(Color.BLACK);

                issuerDetailCount.setText(current+amount);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        presenter = new RegisterAssetPresenter(getContext(), this);
        return rootView;
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
            String max = assetMax.getText().toString();
            int precision = Integer.valueOf(issueAssetPrecision.getText().toString());
            for (int i=0;i<precision;i++){
                max+="0";
            }
            presenter.register(issuerNameEt.getText().toString(),issuerDetailEt.getText().toString(),
                    max,issueAssetPrecision.getText().toString(),
                    password,secondSecret);
            showHUD();
        }
    }

    private boolean hasSecondPwd(){
        return AccountsManager.getInstance().getCurrentAccount().hasSecondSecret();
    }

    @Override
    public void displaySuccess() {
        dismissHUD();
        AppUtil.toastSuccess(getActivity(),getString(R.string.register_asset_success));
        Intent intent = new Intent(getActivity(),IssuerAssetsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void setPresenter(RegisterAssetContract.Presenter presenter) {


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }


}
