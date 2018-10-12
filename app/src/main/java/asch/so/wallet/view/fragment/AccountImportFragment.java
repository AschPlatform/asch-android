package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kaopiz.kprogresshud.KProgressHUD;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.AccountsActivity;
import asch.so.wallet.activity.CheckPasswordActivity;
import asch.so.wallet.activity.ImportOrCreateAccoutActivity;
import asch.so.wallet.activity.MainTabActivity;
import asch.so.wallet.contract.AccountImportContract;
import asch.so.wallet.presenter.AccountImportPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountImportFragment extends BaseFragment implements AccountImportContract.View{

    @BindView(R.id.seed_et)
    EditText seedEt;
    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.import_btn)
    Button importBtn;

    private Unbinder unbinder;
    private AccountImportPresenter presenter;

    KProgressHUD hud=null;

    public static AccountImportFragment newInstance() {
        Bundle args = new Bundle();
        AccountImportFragment fragment = new AccountImportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_account_import,container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Context ctx=this.getContext();

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importAccount();
            }
        });
        this.presenter=new AccountImportPresenter(getContext(),this);

        return rootView;
    }

    /**
     * 创建账户
     */
    private void importAccount(){

        String name = nameEt.getText().toString().trim();
        String seed=seedEt.getText().toString().trim();
        if (TextUtils.isEmpty(seed)){
            AppUtil.toastError(getContext(),getString(R.string.secret_key_null));
            return;
        }
        if (!Validator.check(getContext(), Validator.Type.Secret,seed,getString(R.string.secret_key_error))) {
            return;
        }
        if (!Validator.check(getContext(), Validator.Type.Name,name,getString(R.string.wallet_name_invalid))) {
            return;
        }
        if (AccountsManager.getInstance().hasAccountForName(name)){
            AppUtil.toastError(getContext(),getString(R.string.wallet_name_exist));
            return;
        }

        presenter.importAccount(seed,name);
        showHUD();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unSubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(AccountImportPresenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        dismissHUD();
        AppUtil.toastError(getContext(),exception.getMessage());
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
        if (hud!=null){
            hud.dismiss();
        }
    }

    @Override
    public void displayCheckMessage(String msg) {
        dismissHUD();
        AppUtil.toastError(getContext(),msg);
    }

    @Override
    public void displayImportAccountResult(boolean res, String msg) {
        if (getActivity()==null)
            return;
        dismissHUD();
        if (res) {

            AppUtil.toastSuccess(getContext(), msg);
            //初始化时的导入
            if (getArguments()!=null && ImportOrCreateAccoutActivity.class.getSimpleName().equals(getArguments().getString("clazz"))){
                getActivity().setResult(1);
                getActivity().finish();
            }else if (getArguments()!=null && AccountsActivity.class.getSimpleName().equals(getArguments().getString("clazz"))){
                Intent intent = new Intent(getActivity(), AccountsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }else if (getArguments()!=null && AssetBalanceFragment.class.getSimpleName().equals(getArguments().getString("clazz"))){
                Intent intent = new Intent(getActivity(), MainTabActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
            }

        }else {
            AppUtil.toastError(getContext(),msg);
        }

    }

    //设置种子
    public void setSeed(String seed){
        this.seedEt.setText(seed);
    }

}
