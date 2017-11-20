package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import asch.so.base.activity.ActivityStackManager;
import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.UIException;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.FirstStartActivity;
import asch.so.wallet.activity.MainTabActivity;
import asch.so.wallet.contract.AccountCreateContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountCreateFragment extends BaseFragment implements AccountCreateContract.View{

    Unbinder unbinder;

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

    @BindView(R.id.create_btn)
    Button createBtn;

    private AccountCreateContract.Presenter presenter;

    public static AccountCreateFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountCreateFragment fragment = new AccountCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_create,container,false);
        unbinder= ButterKnife.bind(this,rootView);
         createBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    createAccount();
             }
         });

        this.presenter.generateSeed();
        return rootView;
    }


    /**
     * 创建账户
     */
    private void createAccount(){
        String seed=seedEt.getText().toString().trim();
        String name=nameEt.getText().toString();
        String passwd=passwdEt.getText().toString();
        String passwd2=passwdEt2.getText().toString();
        String hint=hintEt.getText().toString();

        if (!Validator.check(getContext(), Validator.Type.Name,name,"钱包名称不符合要求"))
        {
            return;
        }

        if (AccountsManager.getInstance().hasAccountForSeed(seed)){
            Toast.makeText(getContext(),"此账户以及存在",Toast.LENGTH_SHORT).show();
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


        presenter.storeAccount(seed,name,passwd,hint);

        if (getArguments()!=null && getArguments().getString("clazz").equals(FirstStartActivity.class.getName())){
            Intent intent =new Intent(getActivity(), MainTabActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().finishAll();
        }else {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    public void setPresenter(AccountCreateContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(UIException exception) {

    }

    @Override
    public void resetSeed(String seed) {

        seedEt.setText(seed);
    }
}
