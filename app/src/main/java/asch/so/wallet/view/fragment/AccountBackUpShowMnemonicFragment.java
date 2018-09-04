package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.activity.AccountBackUpCheckOrderActivity;
import asch.so.wallet.activity.AccountBackUpShowMnemonicActivity;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AccountBackUpShowMnemonicFragment extends BaseFragment{

    Unbinder unbinder;
    @BindView(R.id.backup_next)
    Button nextBtn;
    @BindView(R.id.backup_mnemonic)
    TextView mnemonicTv;





    public static AccountBackUpShowMnemonicFragment newInstance() {
        Bundle args = new Bundle();
        AccountBackUpShowMnemonicFragment fragment = new AccountBackUpShowMnemonicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_backup_show_mnemonic,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        mnemonicTv.setText(getArguments().getString("seed"));
        mnemonicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.copyText(getActivity(),mnemonicTv.getText().toString());
            }
        });
        mnemonicTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AppUtil.copyText(getActivity(),mnemonicTv.getText().toString());
                return false;
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.start(getActivity(), AccountBackUpCheckOrderActivity.class,getArguments());
            }
        });
        return rootView;
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }


}
