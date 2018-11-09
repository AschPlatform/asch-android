package asch.io.wallet.view.widget;

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

import asch.io.base.view.Throwable;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.model.entity.Account;
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
 * Created by kimziv on 2018/2/26.
 */

public class DAppPasswordDialog extends Dialog implements View.OnClickListener{

    @BindView(R.id.account_passwd_et)
    EditText accountPasswdEt;
    @BindView(R.id.input_passwd_switch)
    SwitchButton inputPasswdSw;
    @BindView(R.id.ok_btn)
    Button okBtn;

    @BindView(R.id.dialog_close)
    ImageView closeIv;

    @BindView(R.id.title_tv)
    TextView titleTv;

    private OnConfirmationListenner listenner;

    private CompositeSubscription subscriptions;

    public  OnConfirmationListenner getListenner() {
        return listenner;
    }

    public void setListenner(OnConfirmationListenner listenner) {
        this.listenner = listenner;
    }

    public DAppPasswordDialog(@NonNull Context context) {
        super(context, R.style.PasswordDialog);
        subscriptions = new CompositeSubscription();
        initDilaog();
    }

    void initDilaog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_dapp_password);
        ButterKnife.bind(this, this);

        inputPasswdSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        closeIv.setOnClickListener(this);
        okBtn.setOnClickListener(this);
        String pwd=getAccount().getTmpPasswd();
        if (!TextUtils.isEmpty(pwd)){
            accountPasswdEt.setText(pwd);
            inputPasswdSw.setChecked(true);
        }else {
            inputPasswdSw.setChecked(false);
        }
    }

    public DAppPasswordDialog setTitle(String title){
        titleTv.setText(title);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_close:
                dismiss();
                break;
            case R.id.ok_btn: {
                String accountPasswd=accountPasswdEt.getText().toString().trim();
                if (inputPasswdSw.isChecked()){
                    getAccount().setTmpPasswd(accountPasswd);
                }else {
                    getAccount().setTmpPasswd(null);
                }
//                if (!Validator.check(getContext(), Validator.Type.Password,accountPasswd,"账户密码不正确"))
//                {
//                    return;
//                }
//                if (!Validator.check(getContext(), Validator.Type.SecondSecret,secondPasswd,"二级密码不正确"))
//                {
//                    return;
//                }
                checkAccountPasswd(accountPasswd);

            }
            break;
        }
    }

    private void checkAccountPasswd(String accountPasswd) {
        String encryptSecret = getAccount().getEncryptSeed();
        Subscription subscription = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String decryptSecret = Account.decryptSecret(accountPasswd, encryptSecret);
                if (!Validation.isValidSecret(decryptSecret)) {
                    subscriber.onError(new Throwable("1"));
                } else {
                    subscriber.onNext(decryptSecret);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        if ("1".equals(e.getMessage())) {
                            if (listenner!=null){
                                listenner.callback(DAppPasswordDialog.this,null,getContext().getString(R.string.account_password_error));
                            }
                        }
                    }

                    @Override
                    public void onNext(String secret) {
                        if (listenner!=null){
                            listenner.callback(DAppPasswordDialog.this,secret,null);
                        }
                    }
                });
        subscriptions.add(subscription);
    }


    public void show(OnConfirmationListenner listenner) {
        super.show();
        this.setListenner(listenner);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        subscriptions.clear();
    }

    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }

    public interface OnConfirmationListenner {
        void callback(DAppPasswordDialog dialog, String secret, String errMsg);
    }
}
