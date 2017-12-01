package asch.so.wallet.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountImportContract;
import asch.so.wallet.presenter.AccountImportPresenter;
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

    @BindView(R.id.passwd_et)
    EditText passwdEt;

    @BindView(R.id.passwd_et2)
    EditText passwdEt2;

    @BindView(R.id.hint_et)
    EditText hintEt;

    @BindView(R.id.import_btn)
    Button importBtn;

    private Unbinder unbinder;

    private AccountImportPresenter presenter;

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
        String passwd = passwdEt.getText().toString().trim();
        String passwd2=passwdEt2.getText().toString().trim();
        String hint=hintEt.getText().toString().trim();
        String seed=seedEt.getText().toString().trim();
        if (!Validator.check(getContext(), Validator.Type.Secret,seed,"密码格式不符合BIP39规范"))
        {
            return;
        }

        if (AccountsManager.getInstance().hasAccountForSeed(seed)){
            Toast.makeText(getContext(),"此账户以及存在",Toast.LENGTH_SHORT).show();
            return;
        }


        if (!Validator.check(getContext(), Validator.Type.Name,name,"钱包名称不符合要求"))
        {
            return;
        }

        if (AccountsManager.getInstance().hasAccountForName(name)){
            Toast.makeText(getContext(),"此账户名称已存在",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Validator.check(getContext(), Validator.Type.Password,passwd,"请输入不少于8位字符的密码")){
            return;
        }
        if (!passwd.equals(passwd2)){
           Toast.makeText(getContext(),"密码不一致,请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }

        presenter.importAccount(seed,name,passwd,hint);
        Toast.makeText(getContext(),"导入成功",Toast.LENGTH_SHORT).show();
        getActivity().setResult(1);
        getActivity().finish();
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

    }

    @Override
    public void reset() {

    }

    //设置种子
    public void setSeed(String seed){
        this.seedEt.setText(seed);
    }

}
