package asch.io.wallet.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asch.io.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/12/14.
 */

public class AssetTransferAlertDialog extends Dialog implements View.OnClickListener{
    @BindView(R.id.dialog_nickname_state)
    TextView nicknameStateTv;
    @BindView(R.id.dialog_address)
    TextView addressTv;
    @BindView(R.id.dialog_amount)
    TextView amountTv;
    @BindView(R.id.dialog_transfer)
    TextView transferTv;
    @BindView(R.id.dialog_cancel)
    TextView cancelTv;
    @BindView(R.id.dialog_currency)
    TextView currencyTv;
    @BindView(R.id.dialog_nickname_tag)
    TextView nicknameTag;
    @BindView(R.id.dialog_address_tag)
    TextView addressTag;
    @BindView(R.id.dialog_transfer_layout)
    RelativeLayout nicknameLayout;
    @BindView(R.id.dialog_address_layout)
    RelativeLayout addressLayout;

    String address;
    String amount;
    String currency;
    String nickname;
    public AssetTransferAlertDialog(@NonNull Context context,String address,
                                    String amount, String currency,String nickname) {
        super(context);
        this.address = address;
        this.amount = amount;
        this.currency = currency;
        this.nickname = nickname;
        initDilaog();
    }

    void initDilaog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_transfer_confirm);
        ButterKnife.bind(this, this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        cancelTv.setOnClickListener(this);
        if (Validation.isValidAddress(address)){
//            nicknameStateTv.setVisibility(View.GONE);
            nicknameTag.setVisibility(View.GONE);
            nicknameLayout.setVisibility(View.GONE);
            addressTv.setText(address);
        } else {
//            addressTv.setVisibility(View.GONE);
            addressTag.setVisibility(View.GONE);
            addressLayout.setVisibility(View.GONE);
            nicknameStateTv.setText(address);
        }
        amountTv.setText(amount);
        currencyTv.setText("("+currency+")");

    }

    public AssetTransferAlertDialog setOnTransferClickListener(View.OnClickListener listener){
        transferTv.setOnClickListener(listener);
        return this;
    }



    @Override
    public void onClick(View v) {
        if (v==cancelTv){
            dismiss();
        }
    }
}
