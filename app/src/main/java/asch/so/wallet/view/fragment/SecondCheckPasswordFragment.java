package asch.so.wallet.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConfig;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.activity.AccountBackUpAttentionActivity;
import asch.so.wallet.activity.AccountCreateActivity;
import asch.so.wallet.activity.AccountDeleteActivity;
import asch.so.wallet.activity.AccountImportActivity;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.activity.CheckPasswordActivity;
import asch.so.wallet.activity.SecondSecretActivity;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import asch.so.wallet.view.widget.SaveSecondPwdDialog;
import asch.so.widget.edittext.PassWordEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.SecurityException;
import so.asch.sdk.security.SecurityStrategy;


public class SecondCheckPasswordFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.check_pwd_ok)
    Button okBtn;
    PassWordEditText pwdEt;

    public static SecondCheckPasswordFragment newInstance() {
        Bundle args = new Bundle();
        SecondCheckPasswordFragment fragment = new SecondCheckPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_check_second_password,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        pwdEt = new PassWordEditText(getActivity(),R.id.input_pwd_layout,rootView);
        pwdEt.setHint(getString(R.string.input_secondary_password));
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPwd(pwdEt.getText().toString());
            }
        });

        return rootView;
    }

    private void checkPwd(String secondPwd){

        if (!AccountSecurity.checkSecondPassword(secondPwd)){
            AppUtil.toastError(getActivity(),getString(R.string.secondary_password_error));
            return;
        }

        //判断是否【提示】保存二级密码
        Account account = AccountsManager.getInstance().getCurrentAccount();
        int state = account.getSaveSecondPasswordState();
        if (state==Account.STATE_SUGGEST){
            SaveSecondPwdDialog dialog  = new SaveSecondPwdDialog(getActivity(),secondPwd);
            dialog.setOkListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountsManager.getInstance().setSaveSecondPwd(Account.STATE_REMEMBER,getArguments().getString("password"),secondPwd);
                    dialog.dismiss();
                    transfer(secondPwd);
                }
            });
            dialog.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountsManager.getInstance().setSaveSecondPwd(Account.STATE_FORGET,getArguments().getString("password"),"");
                    dialog.dismiss();
                    transfer(secondPwd);
                }
            });
            dialog.show();
        }else {
            transfer(secondPwd);
        }


    }

    void transfer(String pwd){
        //校验是否从转账跳转
        String title = getArguments().getString("title");
        if (TextUtils.isEmpty(title))
            return;

        Intent intent = new Intent();
        intent.putExtra("secondPwd",pwd);
        getActivity().setResult(1,intent);
        getActivity().finish();
        return;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }

}
