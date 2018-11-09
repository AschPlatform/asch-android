package asch.io.wallet.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import asch.io.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/12/14.
 */

public class SaveSecondPwdDialog extends Dialog {
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.ok_btn)
    Button okBtn;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;
    String secondPwd;

    public SaveSecondPwdDialog(@NonNull Context context,String secondPwd) {
        super(context);
        initDialog();
        this.secondPwd = secondPwd;
    }

    void initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_remember_second_password);
        ButterKnife.bind(this, this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setOkListener(View.OnClickListener listener){
        okBtn.setOnClickListener(listener);
    }

    public void setCancelListener(View.OnClickListener listener){
        cancelBtn.setOnClickListener(listener);
    }

    public SaveSecondPwdDialog setTitle(String title){
        titleTv.setText(title);
        return this;
    }

    public SaveSecondPwdDialog setContent(String content){
        contentTv.setText(content);
        return this;
    }





}
