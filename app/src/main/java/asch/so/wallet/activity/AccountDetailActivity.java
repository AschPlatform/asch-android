package asch.so.wallet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseDialogFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountDetailContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountDetailPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.util.IdenticonGenerator;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.validator.Validator;
import asch.so.wallet.view.widget.InputPasswdDialog;
import asch.so.widget.toolbar.BaseToolbar;
import asch.so.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/27.
 */

public class AccountDetailActivity extends BaseActivity implements View.OnClickListener, AccountDetailContract.View{

    @BindView(R.id.toolbar)
    TitleToolbar toolbar;
    @BindView(R.id.backup_btn)
    Button backupBtn;
    @BindView(R.id.delete_btn)
    Button deleteBtn;
    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.balance_tv)
    TextView balanceTv;
    @BindView(R.id.ident_icon)
    ImageView identiconIv;

    AccountDetailContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_detail);
        ButterKnife.bind(this);
        backupBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        addressTv.setOnClickListener(this);
        StatusBarUtil.immersive(this);
        initToolBar();

        float balance=getBundle().getFloat("balance");
        this.balanceTv.setText(String.format("%.3f XAS",balance));

        presenter=new AccountDetailPresenter(this,this);
        presenter.loadAccount(null);
    }

    private void initToolBar(){
        //toolbar = (TitleToolbar) findViewById(R.id.toolbar);
        toolbar.setCloseVisible(false);
        toolbar.setOnOptionItemClickListener(new BaseToolbar.OnOptionItemClickListener() {
            @Override
            public void onOptionItemClick(View v) {
                switch (v.getId()){
                    case R.id.back:
                       onBackPressed();
                        break;
                }
            }
        });
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View view) {

        if (view==backupBtn){
            backupAccount();
        }else if(view==deleteBtn) {
            deleteAccount();
        }else if (view==addressTv){
            copyAddress();
        }
    }

    private void copyAddress(){
        String address=addressTv.getText().toString().trim();
        if (!TextUtils.isEmpty(address)){
            AppUtil.copyText(this,address);
        }
    }

    private boolean hasBackup(){
       return  (AccountsManager.getInstance().getCurrentAccount()!=null) && AccountsManager.getInstance().getCurrentAccount().isBackup();
    }

    private void backupAccount(){
        if (hasBackup()){
            AppUtil.toastError(this,"该账户已经备份过");
            return;
        }
            FragmentManager fm = getSupportFragmentManager();
            InputPasswdDialog dialog = InputPasswdDialog.newInstance();
              AccountDetailActivity thiz=this;
              Account account= presenter.getAccount();
            dialog.setOnClickListener(new BaseDialogFragment.OnClickListener() {
                @Override
                public void onClick(BaseDialogFragment dialog, int which) {
                    EditText editText = dialog.getDialog().findViewById(R.id.passwd_et);
                    String inputPasswd=editText.getText().toString().trim();
                    if ( AccountsManager.getInstance().getCurrentAccount().checKPassword(inputPasswd)){
                        Bundle bundle=new Bundle();
                        bundle.putString("secret",account.getSeed());
                        BaseActivity.start(thiz,SecretBackupActivity.class,bundle);
                        dialog.dismiss();
                    }else {
                        AppUtil.toastError(thiz,"密码输入不正确,请重新输入");
                    }
                }
            });
            dialog.setOnCancelListener(new BaseDialogFragment.OnCancelListener() {
                @Override
                public void onCancel(BaseDialogFragment dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show(fm,"backup_account");
    }

    private void deleteAccount(){
        if (AccountsManager.getInstance().getAccountsCount()<2){
            AppUtil.toastError(this,"当前钱包只有一只账户，不能删除");
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        InputPasswdDialog dialog = InputPasswdDialog.newInstance();
        AccountDetailActivity thiz=this;
        Account account= presenter.getAccount();
        dialog.setOnClickListener(new BaseDialogFragment.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog, int which) {
                EditText editText = dialog.getDialog().findViewById(R.id.passwd_et);
                String inputPasswd=editText.getText().toString().trim();
                if (TextUtils.isEmpty(inputPasswd)){
                    AppUtil.toastError(thiz,"请输入账户密码");
                    return;
                }
                if ( AccountsManager.getInstance().getCurrentAccount().checKPassword(inputPasswd)){
                    AccountsManager.getInstance().removeCurrentAccount();
                    AppUtil.toastSuccess(thiz,"删除成功");
                    dialog.dismiss();
                    Intent intent = new Intent(thiz, AccountsActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    AppUtil.toastError(thiz,"密码输入不正确,请重新输入");
                }
            }
        });
        dialog.setOnCancelListener(new BaseDialogFragment.OnCancelListener() {
            @Override
            public void onCancel(BaseDialogFragment dialog) {
                dialog.dismiss();
            }
        });
        dialog.show(fm,"delete_account");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.item_save:
            {
                String name =nameEt.getText().toString().trim();
                if (Validator.check(this, Validator.Type.Name,name,"账户名称只能是字母数组或者下划线"))
                {
                    this.presenter.changeAccountName(name);
                    AppUtil.toastSuccess(this,"保存成功");
                    finish();
                }
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void setPresenter(AccountDetailContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        //// TODO: 2017/11/6
    }

    @Override
    public void displayAccount(Account account) {
        this.addressTv.setText(account.getAddress());
        this.nameEt.setText(account.getName());
//        this.balanceTv.setText("666 XAS");
        IdenticonGenerator.getInstance().generateBitmap(account.getAddress(), new IdenticonGenerator.OnIdenticonGeneratorListener() {
            @Override
            public void onIdenticonGenerated(Bitmap bmp) {
                identiconIv.setImageBitmap(bmp);
            }
        });
    }
}
