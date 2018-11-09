package asch.io.wallet.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import asch.io.wallet.R;
import asch.io.wallet.activity.SecureSettingActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/12/14.
 */

public class BackUpMenmonicAlertDialog extends Dialog implements View.OnClickListener{
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.ok_btn)
    Button okBtn;
    Context mContext;
    public BackUpMenmonicAlertDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        initDilaog();
    }

    void initDilaog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_backup);
        ButterKnife.bind(this, this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        okBtn.setOnClickListener(this);
    }

    public BackUpMenmonicAlertDialog setTitle(String title){
        titleTv.setText(title);
        return this;
    }

    public BackUpMenmonicAlertDialog setContent(String content){
        contentTv.setText(content);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v==okBtn){
            dismiss();
            Intent intent = new Intent();
            intent.setClass(mContext,SecureSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);

        }
    }
}
