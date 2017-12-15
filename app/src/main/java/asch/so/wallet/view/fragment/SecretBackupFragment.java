package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
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
        showAlertDialog();
        presenter=new SecretBackupPresenter(getContext(),this);
        return rootView;
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder =new AlertDialog
                .Builder(getContext());
        builder.setTitle("重要提示")
        .setMessage("助记词用于恢复账户，只能备份一次，请将它抄下来，并保存在安全的地方！")
        .setCancelable(false)
        .setPositiveButton("确定",null)
        .show();

//        AlertDialog dialog =new AlertDialog(getContext())
//                .setTitle("重要提示")
//                .setContent("助记词用于恢复账户，只能备份一次，请将它抄下来，并保存在安全的地方！");
//        dialog.show();
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
        AppUtil.copyTextWithWarning(getActivity(),secret, "已经复制到粘贴板，但还需您手动备份到安全的地方！");
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
