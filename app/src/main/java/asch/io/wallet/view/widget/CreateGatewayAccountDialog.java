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

public class CreateGatewayAccountDialog extends Dialog implements View.OnClickListener{

    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.ok_btn)
    Button okBtn;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;

    public CreateGatewayAccountDialog(@NonNull Context context) {
        super(context);
        initDilaog();
    }

    void initDilaog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_gateway_account);
        ButterKnife.bind(this, this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        cancelBtn.setOnClickListener(this);
    }



    public CreateGatewayAccountDialog setContent(String content){
        contentTv.setText(content);
        return this;
    }

    public CreateGatewayAccountDialog setOkOnClickListener(View.OnClickListener listener){
        okBtn.setOnClickListener(listener);
        return this;
    }

    @Override
    public void onClick(View v) {

        if (v==cancelBtn){
            dismiss();
        }
    }
}
