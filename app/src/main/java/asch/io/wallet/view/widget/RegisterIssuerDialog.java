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

public class RegisterIssuerDialog extends Dialog implements View.OnClickListener{
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.ok_btn)
    Button okBtn;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;
    public RegisterIssuerDialog(@NonNull Context context) {
        super(context);
        initDilaog();
    }

    void initDilaog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_regist_issuer);
        ButterKnife.bind(this, this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        cancelBtn.setOnClickListener(this);
    }

    public RegisterIssuerDialog setTitle(String title){
        titleTv.setText(title);
        return this;
    }

    public RegisterIssuerDialog setContent(String content){
        contentTv.setText(content);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v==cancelBtn){
            dismiss();
        }
    }

    public RegisterIssuerDialog setOkClickListener(View.OnClickListener listener){
        okBtn.setOnClickListener(listener);
        return this;
    }
}
