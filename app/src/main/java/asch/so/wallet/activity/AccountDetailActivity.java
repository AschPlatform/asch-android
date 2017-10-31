package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import asch.so.base.util.ActivityUtils;
import asch.so.wallet.R;
import asch.so.wallet.contract.AccountDetailContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountDetailPresenter;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.fragment.AccountDetailFragment;
import asch.so.wallet.view.validator.Validator;
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

        AccountDetailActivity thiz=this;
        View clickedView=view;
        Account account= presenter.getAccount();
//        if (view==backupBtn){
//
//        }else if(view==deleteBtn){
            DialogPlus dialog = DialogPlus.newDialog(this)
                    .setContentHolder(new ViewHolder(R.layout.dialog_input_passwd))
                    .setGravity(Gravity.BOTTOM)
                    .setCancelable(false)
                    .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialogPlus, View view) {
                            switch (view.getId()){
                                case R.id.ok_btn:
                                {
                                    if (clickedView==backupBtn){
                                        Bundle bundle=new Bundle();
                                        bundle.putString("secret",account.getSeed());
                                        BaseActivity.start(thiz,SecretBackupActivity.class,bundle);
                                        dialogPlus.dismiss();
                                    }else if(clickedView==deleteBtn) {

                                    }
                                }
                                    break;
                                case R.id.cancel_btn:
                                {
                                    dialogPlus.dismiss();
                                }
                                break;
                            }
                        }
                    })
                    .create();
            dialog.show();
       // }
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
    public void displayAccount(Account account) {
        this.addressTv.setText(account.getAddress());
        this.nameEt.setText(account.getName());
    }
}
