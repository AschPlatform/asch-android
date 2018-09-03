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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SetWalletPwdFragment extends BaseFragment implements View.OnClickListener, SetWalletPwdContract.View{

    Unbinder unbinder;
    @BindView(R.id.passwd_et)
    EditText passwdEt;
    @BindView(R.id.eye_1)
    View eye1;
    @BindView(R.id.clear_1)
    View clear1;

    @BindView(R.id.passwd_et2)
    EditText passwdEt2;
    @BindView(R.id.eye_2)
    View eye2;
    @BindView(R.id.clear_2)
    View clear2;

    @BindView(R.id.create_btn)
    Button createBtn;

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
        presenter=new SetWalletPwdPresenter(getContext(),this);
        eye1.setOnClickListener(this::onClick);
        clear1.setOnClickListener(this::onClick);
        eye2.setOnClickListener(this::onClick);
        clear2.setOnClickListener(this::onClick);
        createBtn.setOnClickListener(this::onClick);
        return rootView;
    }

    private void createWallet(){
        String passwd=passwdEt.getText().toString();
        String passwd2=passwdEt2.getText().toString();

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
            case R.id.eye_1:
                if (passwdEt.getInputType()==InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    passwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwdEt.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    passwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                passwdEt.setSelection(passwdEt.getText().toString().length());
                break;

            case R.id.eye_2:
                if (passwdEt2.getInputType()==InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    passwdEt2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwdEt2.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    passwdEt2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwdEt2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                passwdEt2.setSelection(passwdEt2.getText().toString().length());
                break;

            case R.id.clear_1:
                passwdEt.setText("");
                break;

            case R.id.clear_2:
                passwdEt2.setText("");
                break;

            case R.id.create_btn:
                createWallet();
                break;
        }
    }
}
