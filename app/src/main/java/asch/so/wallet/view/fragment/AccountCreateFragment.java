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
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import asch.so.base.activity.ActivityStackManager;
import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.BackupActivity;
import asch.so.wallet.activity.FirstStartActivity;
import asch.so.wallet.activity.MainTabActivity;
import asch.so.wallet.activity.SecretBackupActivity;
import asch.so.wallet.contract.AccountCreateContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountCreatePresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountCreateFragment extends BaseFragment implements AccountCreateContract.View{

    Unbinder unbinder;

//    @BindView(R.id.seed_et)
//    EditText seedEt;
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

    KProgressHUD hud=null;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_create,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        presenter=new AccountCreatePresenter(getContext(),this);
         createBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    createAccount();
             }
         });

        //this.presenter.generateSeed();
        return rootView;
    }


    /**
     * 创建账户
     */
    private void createAccount(){
       // String seed=seedEt.getText().toString().trim();
        String name=nameEt.getText().toString();
        String passwd=passwdEt.getText().toString();
        String passwd2=passwdEt2.getText().toString();
        String hint=hintEt.getText().toString();

        if (!Validator.check(getContext(), Validator.Type.Name,name,"钱包名称不符合要求"))
        {
            return;
        }

//        if (AccountsManager.getInstance().hasAccountForSeed(seed)){
//            Toast.makeText(getContext(),"此账户以及存在",Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (AccountsManager.getInstance().hasAccountForName(name)){
            AppUtil.toastError(getContext(),"此账户名称已存在");
            return;
        }

        if (!Validator.check(getContext(), Validator.Type.Password,passwd,"请输入不少于8位字符的密码")){
            return;
        }
        if (!passwd.equals(passwd2)){
            AppUtil.toastError(getContext(),"密码不一致,请重新输入");
            return;
        }


        presenter.storeAccount(null,name,passwd,hint);
        showHUD();
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
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
        presenter.unSubscribe();
    }

    @Override
    public void setPresenter(AccountCreateContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        dismissHUD();
        AppUtil.toastError(getContext(),exception.getMessage());
    }

    @Override
    public void displayCheckMessage(String msg) {
        dismissHUD();
        AppUtil.toastError(getContext(),msg);
    }


    @Override
    public void displayCreateAccountResult(boolean res, String msg, String secret) {

        if (getActivity()==null)
            return;
        dismissHUD();
        if (res) {
            AppUtil.toastSuccess(getContext(), msg);
            showSuccessDialog(secret);
        }else {
            AppUtil.toastError(getContext(),msg);
        }
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (getArguments()!=null &&  FirstStartActivity.class.getName().equals(getArguments().getString("clazz"))){
//                    Intent intent =new Intent(getActivity(), MainTabActivity.class);
//                    startActivity(intent);
//                    ActivityStackManager.getInstance().finishAll();
//                }else {
//                    getActivity().finish();
//                }
//            }
//        }, 500);

    }

    private void goback(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              finishPage();
            }
        }, 500);
    }

    private void finishPage(){
        if (getArguments()!=null &&  FirstStartActivity.class.getName().equals(getArguments().getString("clazz"))){
            Intent intent =new Intent(getActivity(), MainTabActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().finishAll();
        }else {
            getActivity().finish();
        }
    }

    private void goBackup(String secret){
        if (secret!=null) {
            Bundle bundle = new Bundle();
            bundle.putString("secret", secret);
            if (getArguments()!=null &&  FirstStartActivity.class.getName().equals(getArguments().getString("clazz"))){
                bundle.putInt("action", SecretBackupActivity.Action.BackupFromStart.getValue());
            }else {
                bundle.putInt("action", SecretBackupActivity.Action.BackupFromInApp.getValue());
            }

            BaseActivity.start(getActivity(), SecretBackupActivity.class, bundle);
        }
    }


    public void showSuccessDialog(String secret){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("安全提示")
                .setCancelable(false)
        .setMessage("尊敬的用户您好，当前账户已经创建成功，为了账户资金安全考虑，务必请您对账户进行备份！")
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                 goBackup(secret);
            }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goback();

            }
        }).show()
        ;
    }
}
