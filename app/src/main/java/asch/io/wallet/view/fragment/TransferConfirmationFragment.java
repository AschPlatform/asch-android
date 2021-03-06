package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/10/16.
 */

public class TransferConfirmationFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.ident_icon)
    ImageView identicon;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.ammount_tv)
    TextView ammountTv;
    @BindView(R.id.transfer_btn)
    Button tranferBtn;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_transfer_confirmation,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        tranferBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view==tranferBtn){
            // TODO: 2017/10/26
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }
}
