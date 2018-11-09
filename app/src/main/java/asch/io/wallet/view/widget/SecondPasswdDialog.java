package asch.io.wallet.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import asch.io.wallet.R;

/**
 * Created by kimziv on 2017/11/15.
 */

public class SecondPasswdDialog extends Dialog implements View.OnClickListener{

    //private TextView mDialogMoney;
    private EditText mEtPwdReal;
    private Button mOKBtn;

    public interface PasswordCallback {
        void callback(SecondPasswdDialog dialog, String password);
    }

    public PasswordCallback mPasswordCallback;

    public SecondPasswdDialog(Context context) {
        super(context, R.style.PasswordDialog);
        initDilaog();
    }

    void initDilaog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.second_secret_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        findViewById(R.id.dialog_close).setOnClickListener(this);
        findViewById(R.id.ok_btn).setOnClickListener(this);

        mEtPwdReal = (EditText) findViewById(R.id.et_pwd_real);
       // mEtPwdReal.addTextChangedListener(new PasswordEditChangedListener(mEtPwdReal));
    }

    private void requestFocus() {
        mEtPwdReal.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 重制
     */
    public void clearPasswordText() {
        mEtPwdReal.setText("");
        requestFocus();
    }

    /**
     * 设置金额
     *
     * @param money
     */
    public void setMoney(int money) {
       // mDialogMoney.setText("¥" + money + ".00");
    }

    /**
     * 设置回调
     *
     * @param passwordCallback
     */
    public void setPasswordCallback(PasswordCallback passwordCallback) {
        this.mPasswordCallback = passwordCallback;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_close:
                dismiss();
                break;
            case R.id.ok_btn:
            {
                String ret = mEtPwdReal.getText().toString().trim();
                if (mPasswordCallback != null)
                    mPasswordCallback.callback(this, ret);
            }
                break;
        }
    }

    private class PasswordEditChangedListener implements TextWatcher {

        private EditText mEditText;

        public PasswordEditChangedListener(EditText editText) {
            this.mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String ret = s.toString().trim();
            if (mEditText.getId() == R.id.et_pwd_real) {
            }
        }

        /**
         * 按删除键后，删除的值对应的图标还原
         *
         * @param length
         * @param editTexts
         */
        void clearTextView(int length, ImageView... editTexts) {
            for (int i = 0; i < 6; i++) {
                //比如当前密码长度为4位，当大于4的图标，就要还原成未输入情况
                if (i > length - 1) {
                    editTexts[i].setImageResource(0);
                }
            }
        }
    }
}
