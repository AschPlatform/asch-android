package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import asch.io.base.activity.BaseActivity;
import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.activity.AccountBackUpShowMnemonicActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AccountBackUpAttentionFragment extends BaseFragment{

    Unbinder unbinder;
    @BindView(R.id.attention_backup)
    Button backupBtn;





    public static AccountBackUpAttentionFragment newInstance() {
        Bundle args = new Bundle();
        AccountBackUpAttentionFragment fragment = new AccountBackUpAttentionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_backup_attention,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.start(getActivity(), AccountBackUpShowMnemonicActivity.class,getArguments());
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
