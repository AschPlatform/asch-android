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
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.util.StatusBarUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/16.
 * 第一次创建钱包的页面
 */

public class ImportOrCreateAccoutActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.create_btn)
    Button createBtn;
    @BindView(R.id.import_btn)
    Button importBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_or_create);

        ButterKnife.bind(this);
        createBtn.setOnClickListener(this);
        importBtn.setOnClickListener(this);

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
            Bundle bundle=new Bundle();
            bundle.putString("clazz",ImportOrCreateAccoutActivity.class.getSimpleName());
            start(this,AccountCreateActivity.class,bundle);
        }else if (view==importBtn){
            Intent intent=new Intent(this,AccountImportActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("clazz",ImportOrCreateAccoutActivity.class.getSimpleName());
            intent.putExtras(bundle);
            startActivityForResult(intent,AccountImportActivity.REQUEST_CODE_FROM_START);
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
