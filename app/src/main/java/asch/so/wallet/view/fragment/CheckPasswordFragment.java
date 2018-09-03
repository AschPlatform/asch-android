package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.activity.AccountBackUpAttentionActivity;
import asch.so.wallet.activity.BackupActivity;
import asch.so.wallet.activity.SecondSecretActivity;
import asch.so.wallet.crypto.AccountSecurity;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CheckPasswordFragment extends BaseFragment{

    Unbinder unbinder;
    @BindView(R.id.check_pwd_ok)
    Button okBtn;
    @BindView(R.id.check_pwd_et)
    EditText pwdEt;




    public static CheckPasswordFragment newInstance() {
        Bundle args = new Bundle();
        CheckPasswordFragment fragment = new CheckPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_check_password,container,false);
        unbinder= ButterKnife.bind(this,rootView);
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
            if (title.equals(getString(R.string.account_backup))){
                String seed = Account.decryptSecret(pwdEt.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putString("seed",seed);
                BaseActivity.start(getActivity(),AccountBackUpAttentionActivity.class,bundle);
            }else if(title.equals(getString(R.string.second_pwd))){
                BaseActivity.start(getActivity(),SecondSecretActivity.class,new Bundle());
            }
        }else {
            AppUtil.toastError(getActivity(),getString(R.string.password_error));
        }

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }


}
