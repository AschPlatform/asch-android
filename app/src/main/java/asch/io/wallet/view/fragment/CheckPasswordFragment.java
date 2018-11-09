package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import asch.io.base.activity.BaseActivity;
import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.accounts.Wallet;
import asch.io.wallet.activity.AccountBackUpAttentionActivity;
import asch.io.wallet.activity.AccountCreateActivity;
import asch.io.wallet.activity.AccountDeleteActivity;
import asch.io.wallet.activity.AccountImportActivity;
import asch.io.wallet.activity.AccountsActivity;
import asch.io.wallet.activity.CheckPasswordActivity;
import asch.io.wallet.activity.SecondCheckPasswordActivity;
import asch.io.wallet.activity.SecondSecretActivity;
import asch.io.wallet.crypto.AccountSecurity;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.util.AppUtil;
import asch.io.widget.edittext.PassWordEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CheckPasswordFragment extends BaseFragment {
    private static final String TAG= CheckPasswordFragment.class.getSimpleName();
    Unbinder unbinder;
    @BindView(R.id.check_pwd_ok)
    Button okBtn;
    PassWordEditText pwdEt;




    public static CheckPasswordFragment newInstance() {
        Bundle args = new Bundle();
        CheckPasswordFragment fragment = new CheckPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (TextUtils.isEmpty(Wallet.getInstance().getEncryptPassword())){
//            Bundle bundle = new Bundle();
//            bundle.putString("title",CheckPasswordFragment.class.getSimpleName());
//            BaseActivity.start(getActivity(),SetWalletPwdActivity.class,bundle);
//            return;
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_check_password,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        pwdEt = new PassWordEditText(getActivity(),R.id.input_pwd_layout,rootView);
        pwdEt.setHint(getString(R.string.enter_wallet_pwd));
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPwd();
            }
        });
        return rootView;
    }



    private void checkPwd(){



        if(Wallet.getInstance().checkPassword(pwdEt.getText().toString())){

            String title = getArguments().getString("title");
            Bundle bundle = new Bundle();

            if (title.equals(getString(R.string.account_backup))){
                String seed = AccountSecurity.decryptSecret(pwdEt.getText().toString());
                bundle.putString("seed",seed);
                BaseActivity.start(getActivity(),AccountBackUpAttentionActivity.class,bundle);
            }else if(title.equals(getString(R.string.set_second_secret))){
                bundle.putString("password",pwdEt.getText().toString());
                BaseActivity.start(getActivity(),SecondSecretActivity.class,bundle);
            }else if(title.equals(AccountsActivity.class.getSimpleName())){
                bundle.putString("clazz",AccountsActivity.class.getSimpleName());
                BaseActivity.start(getActivity(), AccountImportActivity.class,bundle);
            }else if(title.equals(getString(R.string.add_account))){
                bundle.putString("clazz", CheckPasswordActivity.class.getSimpleName());
                BaseActivity.start(getActivity(), AccountCreateActivity.class,bundle);
            }else if(title.equals(getString(R.string.delete_account))){
                bundle.putString("password",pwdEt.getText().toString());
                BaseActivity.start(getActivity(), AccountDeleteActivity.class,bundle);
            }else if(title.equals(AssetBalanceFragment.class.getSimpleName())){
                bundle.putString("clazz",AssetBalanceFragment.class.getSimpleName());
                BaseActivity.start(getActivity(), AccountImportActivity.class,bundle);
            }
            //转账、提现、注册发行商、注册资产。
            else if(title.equals(AssetTransferFragment.class.getSimpleName())
                    ||title.equals(AssetWithdrawFragment.class.getSimpleName())
                    ||title.equals(AssetTransactionsFragment.class.getSimpleName())
                    ||title.equals(RegisterIssuerFragment.class.getSimpleName())
                    ||title.equals(RegisterUIAAssetFragment.class.getSimpleName())
                    ||title.equals(IssueAssetFragment.class.getSimpleName())){

//                需要校验二级密码的情况
                if (getArguments().getBoolean("hasSecondPwd")){

                    Account account = AccountsManager.getInstance().getCurrentAccount();
                    int state = account.getSaveSecondPasswordState();
                    //保存二级密码的情况
                    if (state==Account.STATE_REMEMBER){
                        String secondPwd = AccountsManager.getInstance().getCurrentAccount().getSecondSecret(pwdEt.getText().toString());
                        backToTransfer(pwdEt.getText().toString(),secondPwd);
                        return;
                    }


                    String currency = getArguments().getString("currency");
                    if (!TextUtils.isEmpty(currency)){
                        bundle.putString("currency",currency);
                    }
                    bundle.putString("title",title);
                    bundle.putString("password",pwdEt.getText().toString());
                    Intent intent = new Intent(getActivity(),SecondCheckPasswordActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,1);
                    return;
                }
                backToTransfer(pwdEt.getText().toString(),"");

            }


        }else {
            AppUtil.toastError(getActivity(),getString(R.string.password_error));
        }

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==1){
            String secondPwd= data.getStringExtra("secondPwd");
            if (!TextUtils.isEmpty(secondPwd)){
                Intent intent = new Intent();
                intent.putExtra("secondPwd",secondPwd);
                intent.putExtra("password",pwdEt.getText().toString());
                getActivity().setResult(1,intent);
                getActivity().finish();
            }


        }
    }

    private void backToTransfer(String password,String secondPwd){
        Intent intent = new Intent();
        intent.putExtra("password",password);
        if (!TextUtils.isEmpty(secondPwd))
            intent.putExtra("secondPwd",secondPwd);
        getActivity().setResult(1,intent);
        getActivity().finish();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }

}
