package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.math.BigDecimal;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.activity.CheckPasswordActivity;
import asch.io.wallet.activity.IssuerAssetsActivity;
import asch.io.wallet.contract.IssueAssetContract;
import asch.io.wallet.presenter.IssueAssetPresenter;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IssueAssetFragment extends BaseFragment implements IssueAssetContract.View {
    Unbinder unbinder;
    KProgressHUD hud;
    IssueAssetContract.Presenter presenter;

    String maxIssue ="";
    String name;
    String amount;
    int precision;

    @BindView(R.id.issuer_name_et)
    EditText issueAssetNum;
    @BindView(R.id.max_issue_tv)
    TextView maxIssueTv;
    @OnClick(R.id.issue_asset_btn) void onIssueAssetBtnClick(){
        amount = issueAssetNum.getText().toString();
        amount = AppUtil.scaleStringFromBigAmount(amount,precision);

        if (TextUtils.isEmpty(name)){
            AppUtil.toastError(getActivity(),"invalid coin name");
            return;
        }
        if (amount.contains(".")) {
            AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_amount));
            return;
        }

        try {

            BigDecimal max= new BigDecimal(maxIssue);
            BigDecimal bigAmount = new BigDecimal(issueAssetNum.getText().toString());
            if (bigAmount.compareTo(max)==1){
                AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_over_max));
                return;
            }
            if (bigAmount.compareTo(BigDecimal.ZERO)==0){
                AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_zero));
                return;
            }

        }catch (Exception e){
            AppUtil.toastError(getActivity(),getString(R.string.error_network));
        }


        Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
        Bundle bundle = new Bundle();
        String clazz = IssueAssetFragment.class.getSimpleName();
        bundle.putString("title",clazz);
        bundle.putBoolean("hasSecondPwd",hasSecondPwd());
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View rootView =inflater.inflate(R.layout.fragment_issue_asset,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        presenter = new IssueAssetPresenter(getContext(),this);
        maxIssue = getArguments().getString("max","");
        name = getArguments().getString("name");
        precision = getArguments().getInt("precision");
        maxIssueTv.setText(maxIssue);


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

            presenter.issueAsset(name,amount,password,secondSecret);
            showHUD();
        }
    }

    private boolean hasSecondPwd(){
        return AccountsManager.getInstance().getCurrentAccount().hasSecondSecret();
    }
    public static IssueAssetFragment newInstance() {
        
        Bundle args = new Bundle();
        IssueAssetFragment fragment = new IssueAssetFragment();
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void displaySuccess() {
        dismissHUD();
        AppUtil.toastSuccess(getActivity(),getString(R.string.issue_asset_success));
        Intent intent = new Intent(getActivity(),IssuerAssetsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void setPresenter(IssueAssetContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayError(Throwable exception) {
        AppUtil.toastError(getContext(),exception.getMessage().toString());
        dismissHUD();
    }
}
