package asch.io.wallet.view.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.arjsna.passcodeview.PassCodeView;

/**
 * Created by kimziv on 2017/10/23.
 */

public class PincodeSettingFragment extends BaseFragment {

    @BindView(R.id.promptview)
    TextView promptView;
    @BindView(R.id.pass_code_view)
    PassCodeView passCodeView;
    Unbinder unbinder;
    public static PincodeSettingFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PincodeSettingFragment fragment = new PincodeSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pincode,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "Font-Bold.ttf");
        passCodeView.setTypeFace(typeFace);
        passCodeView.setKeyTextColor(R.color.black);
        passCodeView.setEmptyDrawable(R.drawable.empty_dot);
        passCodeView.setFilledDrawable(R.drawable.filled_dot);
        promptView.setTypeface(typeFace);
        bindEvents();
        return rootView;
    }

    private void bindEvents() {
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override public void onTextChanged(String text) {
                if (text.length() == 6) {
                    if (text.equals("123456")) {
//                        Intent intent = new Intent(getActivity(), LoggedInActivity.class);
//                        startActivity(intent);
//                        getActivity().finish();
                    } else {
                        passCodeView.setError(true);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }
}
