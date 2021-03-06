package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.contract.SecretBackupContract;
import asch.io.wallet.crypto.AccountSecurity;
import asch.io.wallet.presenter.SecretBackupPresenter;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/10/17.
 */

public class SecretBackupFragment extends BaseFragment implements SecretBackupContract.View, View.OnClickListener{


    @BindView(R.id.copy_btn)
    Button copyBtn;
    @BindView(R.id.secret_tv)
    TextView secretTv;
    Unbinder unbinder;
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
        unbinder = ButterKnife.bind(this, rootView);
        copyBtn.setOnClickListener(this);
        String password=getArguments().getString("password");
        secretTv.setText(AccountSecurity.decryptSecret(password));
        showAlertDialog();
        presenter=new SecretBackupPresenter(getContext(),this);
        return rootView;
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder =new AlertDialog
                .Builder(getContext());
        builder.setTitle(getString(R.string.important_note))
        .setMessage(getString(R.string.note_content))
        .setCancelable(false)
        .setPositiveButton(getString(R.string.confirm),null)
        .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        if (view==copyBtn){
            copySecret(secretTv.getText().toString().trim());
        }
    }

    private void copySecret(String secret){
        presenter.backupSecret(secret);
        AppUtil.copyTextWithWarning(getActivity(),secret, getString(R.string.sure_copy));
    }

    @Override
    public void setPresenter(SecretBackupContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {

    }

    @Override
    public void displaySecret(String secret) {

    }
}
