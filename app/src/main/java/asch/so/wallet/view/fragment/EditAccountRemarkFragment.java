package asch.so.wallet.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import asch.so.base.activity.ActivityStackManager;
import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConfig;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.AccountsActivity;
import asch.so.wallet.activity.CheckPasswordActivity;
import asch.so.wallet.activity.ImportOrCreateAccoutActivity;
import asch.so.wallet.activity.MainTabActivity;
import asch.so.wallet.activity.SecretBackupActivity;
import asch.so.wallet.contract.AccountCreateContract;
import asch.so.wallet.presenter.AccountCreatePresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class EditAccountRemarkFragment extends BaseFragment{

    Unbinder unbinder;

    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.create_btn)
    Button createBtn;
    @BindView(R.id.nickname_tv)
    TextView nicknameTv;



    public static EditAccountRemarkFragment newInstance() {

        Bundle args = new Bundle();
        EditAccountRemarkFragment fragment = new EditAccountRemarkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_edit_account_remark,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        createBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    createAccount();
             }});
        nicknameTv.setText(AccountsManager.getInstance().getCurrentAccount().getFullAccount().getAccount().getName());
        nameEt.setText(AccountsManager.getInstance().getCurrentAccount().getName());
        return rootView;
    }


    /**
     * 创建账户
     */
    private void createAccount(){
        String name=nameEt.getText().toString();

        if (!Validator.check(getContext(), Validator.Type.Name,name,getString(R.string.wallet_name_invalid))) {
            return;
        }
        if (AccountsManager.getInstance().hasAccountForName(name)){
            AppUtil.toastError(getContext(),getString(R.string.wallet_name_exist));
            return;
        }
        AccountsManager.getInstance().updateAccount(name);
        AppUtil.toastSuccess(getActivity(),getString(R.string.opreation_success));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
            }
        },1000);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }






}
