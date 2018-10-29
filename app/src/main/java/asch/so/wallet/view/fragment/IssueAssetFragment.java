package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.webkit.WebView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.P;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.CheckPasswordActivity;
import asch.so.wallet.activity.IssuerAssetsActivity;
import asch.so.wallet.contract.IssueAssetContract;
import asch.so.wallet.presenter.IssueAssetPresenter;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IssueAssetFragment extends BaseFragment implements IssueAssetContract.View {

    @BindView(R.id.issuer_name_et)
    EditText issueAssetNum;
    @BindView(R.id.max_issue_tv)
    TextView maxIssueTv;
    @OnClick(R.id.issue_asset_btn) void onIssueAssetBtnClick(){
        String num = issueAssetNum.getText().toString();
        name = getArguments().getString("name");
        if (TextUtils.isEmpty(name)){
            AppUtil.toastError(getActivity(),getString(R.string.error_network));
            return;
        }
        if (num.contains(".")) {
            AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_amount));
            return;
        }
        try {
            amount = Integer.valueOf(num);
            int max= Integer.valueOf(maxIssue);
            if (amount>max){
                AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_over_max));
            }
            if (amount==0)
                AppUtil.toastError(getActivity(),getString(R.string.err_issue_asset_zero));
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
    Unbinder unbinder;
    KProgressHUD hud;
    IssueAssetContract.Presenter presenter;

    String maxIssue ="";
    String name;
    int amount = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View rootView =inflater.inflate(R.layout.fragment_issue_asset,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        presenter = new IssueAssetPresenter(getContext(),this);
        maxIssue = getArguments().getString("max","");
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

            presenter.issueAsset(name,String.valueOf(amount),password,secondSecret);
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
