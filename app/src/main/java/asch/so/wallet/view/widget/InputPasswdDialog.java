package asch.so.wallet.view.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import asch.so.base.fragment.BaseDialogFragment;
import asch.so.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.me.lewisdeane.ldialogs.BaseDialog;

/**
 * Created by kimziv on 2017/11/3.
 */

public class InputPasswdDialog extends BaseDialogFragment implements View.OnClickListener{

    @BindView(R.id.passwd_et)
    EditText editText;
    @BindView(R.id.ok_btn)
    Button okBtn;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;

    private BaseDialogFragment.OnCancelListener onCancelListener;
    private BaseDialogFragment.OnClickListener onClickListener;
    private BaseDialogFragment.OnDismissListener onDismissListener;


    public static InputPasswdDialog newInstance() {
        
        Bundle args = new Bundle();
        
        InputPasswdDialog fragment = new InputPasswdDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public InputPasswdDialog() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_input_passwd);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        ButterKnife.bind(this, dialog);

         okBtn.setOnClickListener(this);
         cancelBtn.setOnClickListener(this);

//        String address = getArguments().getString("address");
//        String amount = getArguments().getString("amount");
//        String currency = getArguments().getString("currency");

        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ok_btn:
            {
                if (this.onClickListener!=null){
                    this.onClickListener.onClick(this,1);
                }

            }
                break;
            case R.id.cancel_btn:
            {
                if (this.onCancelListener!=null){
                    this.onCancelListener.onCancel(this);
                }
            }
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.onDismissListener!=null){
            this.onDismissListener.onDismiss(this);
        }
    }


    public OnCancelListener getOnCancelListener() {
        return onCancelListener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
