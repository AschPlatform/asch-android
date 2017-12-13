package asch.so.wallet.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import asch.so.base.view.Throwable;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.Validation;

/**
 * Created by kimziv on 2017/12/12.
 */

public class AllPasswdsDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.second_passwd_et)
    EditText secondPasswdEt;
    @BindView(R.id.account_passwd_et)
    EditText accountPasswdEt;
    @BindView(R.id.ok_btn)
    Button okBtn;

    @BindView(R.id.dialog_close)
    ImageView closeIv;
    private OnConfirmationListenner listenner;

    private CompositeSubscription subscriptions;

    public OnConfirmationListenner getListenner() {
        return listenner;
    }

    public void setListenner(OnConfirmationListenner listenner) {
        this.listenner = listenner;
    }

    public AllPasswdsDialog(@NonNull Context context, boolean needSecondPasswd) {
        super(context, R.style.PasswordDialog);
        subscriptions = new CompositeSubscription();
        initDilaog(needSecondPasswd);
    }

    void initDilaog(boolean needSecondPasswd) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_all_passwords);
        ButterKnife.bind(this, this);
        if (needSecondPasswd) {
            secondPasswdEt.setVisibility(View.VISIBLE);
        } else {
            secondPasswdEt.setVisibility(View.GONE);
        }

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        closeIv.setOnClickListener(this);
        okBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_close:
                dismiss();
                break;
            case R.id.ok_btn: {
                String accountPasswd=accountPasswdEt.getText().toString().trim();
                String secondPasswd=secondPasswdEt.getText().toString().trim();
//                if (!Validator.check(getContext(), Validator.Type.Password,accountPasswd,"账户密码不正确"))
//                {
//                    return;
//                }
//                if (!Validator.check(getContext(), Validator.Type.SecondSecret,secondPasswd,"二级密码不正确"))
//                {
//                    return;
//                }
                checkAccountPasswd(accountPasswd, secondPasswd);

            }
            break;
        }
    }

    private void checkAccountPasswd(String accountPasswd, String secondSecret) {
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
                           // AppUtil.toastError(getContext(), "账户密码不正确");
                            if (listenner!=null){
                                listenner.callback(AllPasswdsDialog.this,null,null,"账户密码不正确");
                            }
                        }

                    }

                    @Override
                    public void onNext(String secret) {
                        if (listenner!=null){
                            listenner.callback(AllPasswdsDialog.this,secret,secondSecret,null);
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
        void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg);
    }
}
