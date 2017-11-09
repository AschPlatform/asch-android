package asch.so.wallet.view.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import asch.so.base.fragment.BaseDialogFragment;
import asch.so.wallet.R;
import asch.so.wallet.util.IdenticonGenerator;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kimziv on 2017/10/30.
 */

public class TransferConfirmationDialog extends BaseDialogFragment implements View.OnClickListener{

    @BindView(R.id.ident_icon)
    ImageView identicon;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.ammount_tv)
    TextView ammountTv;
    @BindView(R.id.transfer_btn)
    Button tranferBtn;

    private OnConfirmListener onClickListener;
    private OnDismissListener onDismissListener;


    public static TransferConfirmationDialog newInstance(String address, String amount, String currency) {
        
        Bundle args = new Bundle();
        args.putString("address",address);
        args.putString("amount",amount);
        args.putString("currency",currency);
        
        TransferConfirmationDialog fragment = new TransferConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public TransferConfirmationDialog() {
        super();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_transfer_confirmation);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        ButterKnife.bind(this, dialog);
        this.tranferBtn.setOnClickListener(this);
        String address = getArguments().getString("address");
        String amount = getArguments().getString("amount");
        String currency = getArguments().getString("currency");
        setConfirmInfo(address,amount,currency);
        return dialog;
    }

    public OnConfirmListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnConfirmListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onClick(View view) {
        if (view==tranferBtn){
            if (this.onClickListener!=null){
               this.onClickListener.onConfirm(this);
            }
        }
    }

    public interface OnConfirmListener {
        void onConfirm(TransferConfirmationDialog dialog);
    }

    public interface OnDismissListener{
        void onDismiss(TransferConfirmationDialog dialog);
    }

    public void setConfirmInfo(String address, String ammount, String currency){
        this.addressTv.setText(address);
        this.ammountTv.setText(ammount+" "+currency);
        IdenticonGenerator.getInstance().generateBitmap(address, new IdenticonGenerator.OnIdenticonGeneratorListener() {
            @Override
            public void onIdenticonGenerated(Bitmap bmp) {
                identicon.setImageBitmap(bmp);
            }
        });
    }
}
