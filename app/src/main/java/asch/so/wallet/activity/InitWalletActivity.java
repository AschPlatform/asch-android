package asch.so.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import asch.so.base.activity.ActivityStackManager;
import asch.so.base.activity.BaseActivity;
import asch.so.wallet.R;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.util.StatusBarUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 2018年08月17日18:26:07
 *
 * 新版初始化钱包。
 *
 * 第一次进入时显示。
 */

public class InitWalletActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.create_btn)
    Button createBtn;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.term_tv)
    TextView termTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_wallet);

        ButterKnife.bind(this);
        createBtn.setOnClickListener(this);

        checkBox.setOnClickListener(this);
        termTv.setOnClickListener(this);
        StatusBarUtil.immersive(this);
        AppUtil.updateApp(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view==createBtn){
            if (checkBox.isChecked()){
                Bundle bundle=new Bundle();
                bundle.putString("clazz",InitWalletActivity.class.getName());
                if (Wallet.getInstance().isSetPwd()){
                    start(this,ImportOrCreateAccoutActivity.class,bundle);
                }else {
                    start(this,SetWalletPwdActivity.class,bundle);
                }

            }else {
                AppUtil.toastWarning(this,getString(R.string.read_service));
            }

        }else if (view==termTv){
            Intent intent=new Intent(this,TermServiceActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==AccountImportActivity.REQUEST_CODE_FROM_START && resultCode==1){
            ActivityStackManager.getInstance().finishAll();
            Intent intent =new Intent(this, MainTabActivity.class);
            startActivity(intent);
            ActivityStackManager.getInstance().finishAll();
        }
    }
}
