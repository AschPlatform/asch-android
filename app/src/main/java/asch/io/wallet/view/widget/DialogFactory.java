package asch.io.wallet.view.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import asch.io.wallet.R;

/**
 * Created by kimziv on 2017/10/30.
 */

public class DialogFactory {

    public static DialogPlus createTransferConfirmationDialog(Context context){
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.fragment_transfer_confirmation))
                .setGravity(Gravity.TOP)
                .setCancelable(true)
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialogPlus, View view) {
                        switch (view.getId()){
                            case R.id.ok_btn:
                            {
                                dialogPlus.dismiss();
                            }
                            break;
                            case R.id.cancel_btn:
                            {
                                dialogPlus.dismiss();
                            }
                            break;
                        }
                    }
                })
                .create();
        return dialog;
    }
}
