package asch.io.base.fragment;

import android.support.v7.app.AppCompatDialogFragment;


/**
 * Created by kimziv on 2017/10/16.
 */

public class BaseDialogFragment extends AppCompatDialogFragment {


    public interface OnMultiChoiceClickListener {
        void onClick(BaseDialogFragment dialog, int which, boolean checked);
    }

    public interface OnClickListener {
        void onClick(BaseDialogFragment dialog, int which);
    }

    public interface OnShowListener {
        void onShow(BaseDialogFragment dialog);
    }

    public interface OnDismissListener {
        void onDismiss(BaseDialogFragment dialog);
    }

    public interface OnCancelListener {
        void onCancel(BaseDialogFragment dialog);
    }
}
