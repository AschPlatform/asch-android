package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kaopiz.kprogresshud.KProgressHUD;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.ImportOrCreateAccoutActivity;
import asch.so.wallet.contract.SetWalletPwdContract;
import asch.so.wallet.presenter.SetWalletPwdPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import asch.so.widget.edittext.PassWordEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SetWalletPwdFragment extends BaseFragment implements View.OnClickListener, SetWalletPwdContract.View{

    @BindView(R.id.create_btn)
    Button createBtn;
    PassWordEditText pwdEt;
    PassWordEditText pwdEt2;
    Unbinder unbinder;

    KProgressHUD hud=null;
    private SetWalletPwdContract.Presenter presenter;

    public static SetWalletPwdFragment newInstance() {
        Bundle args = new Bundle();
        SetWalletPwdFragment fragment = new SetWalletPwdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_set_wallet_pwd,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        createBtn.setOnClickListener(this);
        presenter=new SetWalletPwdPresenter(getContext(),this);
        pwdEt = new PassWordEditText(getActivity(),R.id.set_pwd_edit1,rootView);
        pwdEt.setHint(getString(R.string.wallet_pwd));
        pwdEt2 = new PassWordEditText(getActivity(),R.id.set_pwd_edit2,rootView);
        pwdEt2.setHint(getString(R.string.ensure_wallet_pwd));
        return rootView;
    }

    private void createWallet(){
        String passwd=pwdEt.getText().toString();
        String passwd2=pwdEt2.getText().toString();


        if (!Validator.check(getContext(), Validator.Type.SecondSecret,passwd,getString(R.string.password_unvalid))){
            return;
        }
        if (!passwd.equals(passwd2)){
            AppUtil.toastError(getContext(),getString(R.string.password_inconsistency));
            return;
        }
        presenter.createWallet(passwd);
        showHUD();
    }

    private  void  showHUD(){
        if (hud==null){
            hud = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .show();
        }
    }

    private  void  dismissHUD(){
        if (hud!=null){
            hud.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
        presenter.unSubscribe();
    }

    @Override
    public void setPresenter(SetWalletPwdContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        dismissHUD();
        AppUtil.toastError(getContext(),exception.getMessage());
    }

    @Override
    public void displayCheckMessage(String msg) {
        dismissHUD();
        AppUtil.toastError(getContext(),msg);
    }

    @Override
    public void displayCreateAccountResult(boolean res, String msg, String secret) {
        if (getActivity()==null)
            return;

        if (res) {
            BaseActivity.start(getActivity(), ImportOrCreateAccoutActivity.class, new Bundle());
            getActivity().finish();
        }else {
            AppUtil.toastError(getContext(),msg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.create_btn:
                createWallet();
                break;
        }
    }
}
