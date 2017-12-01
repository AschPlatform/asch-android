package asch.so.wallet.view.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.UIException;
import asch.so.wallet.R;
import asch.so.wallet.contract.SecretBackupContract;
import asch.so.wallet.presenter.SecretBackupPresenter;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/17.
 */

public class SecretBackupFragment extends BaseFragment implements SecretBackupContract.View, View.OnClickListener{


    @BindView(R.id.copy_btn)
    Button copyBtn;
    @BindView(R.id.secret_tv)
    TextView secretTv;

    SecretBackupContract.Presenter presenter;

    public static SecretBackupFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SecretBackupFragment fragment = new SecretBackupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView =inflater.inflate(R.layout.fragment_secret_backup,container,false);
        ButterKnife.bind(this,rootView);
        copyBtn.setOnClickListener(this);
       String secret=getArguments().getString("secret");
        secretTv.setText(secret);

        presenter=new SecretBackupPresenter(getContext(),this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
    }

    @Override
    public void onClick(View view) {
        if (view==copyBtn){
            copySecret(secretTv.getText().toString().trim());
        }
    }

    private void copySecret(String secret){
        presenter.backupSecret(secret);
        AppUtil.copyText(getActivity(),secret);

//        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//        clipboardManager.setText(secret);
//        presenter.backupSecret(secret);
//        Toast.makeText(getContext(),"复制成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(SecretBackupContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(UIException exception) {

    }

    @Override
    public void displaySecret(String secret) {

    }
}
