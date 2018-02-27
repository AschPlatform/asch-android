package asch.so.wallet.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2018/2/27.
 */

public class AuthorizeDialog extends Dialog implements View.OnClickListener{

    @BindView(R.id.icon_iv)
    ImageView iconIv;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    @BindView(R.id.ok_btn)
    Button okBtn;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;

    private OnConfirmationListenner listenner;


    public OnConfirmationListenner getListenner() {
        return listenner;
    }

    public void setListenner(OnConfirmationListenner listenner) {
        this.listenner = listenner;
    }

    public AuthorizeDialog(@NonNull Context context) {
        super(context, R.style.PasswordDialog);
        initDilaog();
    }

    void initDilaog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_authorize);
        ButterKnife.bind(this, this);


        setCancelable(false);
        setCanceledOnTouchOutside(false);

        cancelBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);

    }

    public AuthorizeDialog setIcon(int resId){
        iconIv.setImageResource(resId);
        return this;
    }

    public AuthorizeDialog setHint(String hint){
        hintTv.setText(hint);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                dismiss();
                if (this.listenner!=null){
                    this.listenner.onCancel(this);
                }
                break;
            case R.id.ok_btn: {
                if (this.listenner!=null){
                    this.listenner.onAuthorize(this);
                }
            }
            break;
        }
    }


    public void show(OnConfirmationListenner listenner) {
        super.show();
        this.setListenner(listenner);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    public interface OnConfirmationListenner {
        void onAuthorize(AuthorizeDialog dialog);
        void onCancel(AuthorizeDialog dialog);
    }
}
