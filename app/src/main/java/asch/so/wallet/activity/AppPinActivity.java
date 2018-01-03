package asch.so.wallet.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;

import asch.so.base.activity.ActivityStackManager;
import asch.so.wallet.R;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.widget.toolbar.BaseToolbar;
import asch.so.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import uk.me.lewisdeane.ldialogs.BaseDialog;
import uk.me.lewisdeane.ldialogs.CustomDialog;

/**
 * Created by kimziv on 2017/10/31.
 */

public class AppPinActivity extends AppLockActivity {

//    @BindView(R.id.toolbar)
//    TitleToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int type=getIntent().getIntExtra(AppLock.EXTRA_TYPE,-1);
        if (type==AppLock.ENABLE_PINLOCK){
            //取消标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //取消状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);

        initToolBar();
        StatusBarUtil.immersive(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    private void initToolBar(){
        TitleToolbar toolbar = (TitleToolbar) findViewById(R.id.toolbar);
        toolbar.setCloseVisible(false);
        toolbar.setOnOptionItemClickListener(new BaseToolbar.OnOptionItemClickListener() {
            @Override
            public void onOptionItemClick(View v) {
                switch (v.getId()){
                    case R.id.back:
                        finish();
                        break;
                }
            }
        });

        int type=getIntent().getIntExtra(AppLock.EXTRA_TYPE,-1);
        if (type==AppLock.ENABLE_PINLOCK){
        toolbar.setTitle(getString(R.string.set_pin));
        }else{
            toolbar.setVisibility(View.GONE);
        }

        //setSupportActionBar(toolbar);
        View forgetView=  findViewById(R.id.pin_code_forgot_textview);
        forgetView.setVisibility(View.GONE);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_apppin;
    }

    @Override
    public void showForgotDialog() {
        Resources res = getResources();
        // Create the builder with required paramaters - Context, Title, Positive Text
        CustomDialog.Builder builder = new CustomDialog.Builder(this, getString(R.string.forget_pin),getString(R.string.confirm));
        builder.content(getString(R.string.pin_tip));
        builder.negativeText(getString(R.string.cancel));

        //Set theme
        builder.darkTheme(false);
        builder.typeface(Typeface.SANS_SERIF);
        builder.positiveColor("#03a9f4"); // int res, or int colorRes parameter versions available as well.
        builder.negativeColor("#03a9f4");
        builder.rightToLeft(false); // Enables right to left positioning for languages that may require so.
        builder.titleAlignment(BaseDialog.Alignment.CENTER);
        builder.buttonAlignment(BaseDialog.Alignment.CENTER);
        builder.setButtonStacking(false);

        //Set text sizes
        builder.titleTextSize((int) res.getDimension(R.dimen.activity_dialog_title_size));
        builder.contentTextSize((int) res.getDimension(R.dimen.activity_dialog_content_size));
        builder.positiveButtonTextSize((int) res.getDimension(R.dimen.activity_dialog_positive_button_size));
        builder.negativeButtonTextSize((int) res.getDimension(R.dimen.activity_dialog_negative_button_size));

        //Build the dialog.
        CustomDialog customDialog = builder.build();
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setClickListener(new CustomDialog.ClickListener() {
            @Override
            public void onConfirmClick() {
                AppUtil.toastInfo(getApplicationContext(), getString(R.string.confirm));
            }

            @Override
            public void onCancelClick() {
                AppUtil.toastInfo(getApplicationContext(), getString(R.string.cancel));
            }
        });

        // Show the dialog.
        customDialog.show();
    }

    @Override
    public void onBackPressed() {
        int type=getIntent().getIntExtra(AppLock.EXTRA_TYPE,-1);
        if (type==AppLock.ENABLE_PINLOCK){
            finish();
        }else{
            ActivityStackManager.getInstance().finishAll();
            finish();
        }
    }

    @Override
    public void onPinFailure(int attempts) {

    }

    @Override
    public void onPinSuccess(int attempts) {

    }

    @Override
    public int getPinLength() {
        return super.getPinLength();//you can override this method to change the pin length from the default 4
    }
}
