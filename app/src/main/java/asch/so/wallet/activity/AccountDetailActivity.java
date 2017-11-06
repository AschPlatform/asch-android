package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseDialogFragment;
import asch.so.base.util.ActivityUtils;
import asch.so.base.view.UIException;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.AccountDetailContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountDetailPresenter;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.fragment.AccountDetailFragment;
import asch.so.wallet.view.validator.Validator;
import asch.so.wallet.view.widget.InputPasswdDialog;
import asch.so.wallet.view.widget.TransferConfirmationDialog;
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

    AccountDetailContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_detail);
        ButterKnife.bind(this);
        backupBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
       // AccountDetailFragment fragment = AccountDetailFragment.newInstance();
        //ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment, R.id.fragment_container);
        StatusBarUtil.immersive(this);
        initToolBar();

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
        }
    }

    private void backupAccount(){
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
                        Toast.makeText(thiz,"密码输入不正确,请重新输入",Toast.LENGTH_SHORT).show();
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
                    AccountsManager.getInstance().removeCurrentAccount();
                    Toast.makeText(thiz,"删除成功",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Intent intent = new Intent(thiz, AccountsActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(thiz,"密码输入不正确,请重新输入",Toast.LENGTH_SHORT).show();
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
                if (Validator.check(this, Validator.Type.Name,name,"账户名称不能为空"))
                {
                    this.presenter.changeAccountName(name);
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(AccountDetailContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(UIException exception) {
        //// TODO: 2017/11/6
    }

    @Override
    public void displayAccount(Account account) {
        this.addressTv.setText(account.getAddress());
        this.nameEt.setText(account.getName());
    }
}
