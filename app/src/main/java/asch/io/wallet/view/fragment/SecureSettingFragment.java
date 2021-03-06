package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import asch.io.base.activity.BaseActivity;
import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.activity.CheckPasswordActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SecureSettingFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.account_backup)
    TextView backUpTv;
    @BindView(R.id.second_pwd)
    TextView pwdTv;
    @BindView(R.id.second_pwd_state)
    TextView pwdStateTv;
    @BindView(R.id.second_backup_state)
    TextView backupStateTv;
    @BindView(R.id.back_arrow)
    ImageView backupArrow;
    @BindView(R.id.second_pwd_arrow)
    ImageView pwdArrow;
    Unbinder unbinder;


    public static SecureSettingFragment newInstance() {
        Bundle args = new Bundle();
        SecureSettingFragment fragment = new SecureSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_security_setting,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        if (getAccount().hasSecondSecret()|
                getAccount().getFullAccount().getAccount().isSecondSignature()){
            pwdStateTv.setText(getString(R.string.have_set));
            pwdArrow.setVisibility(View.GONE);
        }else {
            pwdTv.setOnClickListener(this);
            pwdStateTv.setText(getString(R.string.set_now));
            pwdArrow.setVisibility(View.VISIBLE);
        }


//        if (AccountsManager.getInstance().getCurrentAccount().isBackup()){
//            backupStateTv.setText(R.string.have_backup);
//            backupArrow.setVisibility(View.GONE);
//        }else {
            backupArrow.setVisibility(View.VISIBLE);
            backupStateTv.setText(R.string.backup_now);
            backUpTv.setOnClickListener(this);
//        }

        return rootView;
    }


    private asch.io.wallet.model.entity.Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }



    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.account_backup:
                bundle.putString("title",getString(R.string.account_backup));
                BaseActivity.start(getActivity(),CheckPasswordActivity.class,bundle);
                break;

            case R.id.second_pwd:
                bundle.putString("title",getString(R.string.set_second_secret));
                BaseActivity.start(getActivity(),CheckPasswordActivity.class,bundle);
                break;


        }
    }
}
