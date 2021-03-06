package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kaopiz.kprogresshud.KProgressHUD;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.activity.SecureSettingActivity;
import asch.io.wallet.contract.SecondSecretContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.presenter.SecondSecretPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.FeeCalculater;

/**
 * Created by haizeiwang on 2018/1/22.
 */
public class SecondSecretFragment extends BaseFragment implements SecondSecretContract.View {

    KProgressHUD hud=null;
    private SecondSecretContract.Presenter presenter;
    @BindView(R.id.passwd_et1)
    EditText passwdEt1;
    @BindView(R.id.passwd_et2)
    EditText passwdEt2;
    @BindView(R.id.save_btn)
    Button save_btn;
    private Unbinder unbinder;


    public static SecondSecretFragment newInstance() {
        SecondSecretFragment fragment = new SecondSecretFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_second_secret, container, false);
        unbinder= ButterKnife.bind(this,rootView);
        presenter=new SecondSecretPresenter(getContext(),this);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSecondPassword();
            }
        });
        return rootView;
    }

    public void saveSecondPassword(){
        String passwd=passwdEt1.getText().toString();
        String passwd2=passwdEt2.getText().toString();
        if (AssetManager.getInstance().queryAschAssetByName(AppConstants.XAS_NAME).getLongBalance() <= FeeCalculater.calcFee(TransactionType.basic_setPassword)){
            AppUtil.toastError(getContext(),getString(R.string.account_balance_insufficient));
            return;
        }

        if (!Validator.check(getContext(), Validator.Type.SecondSecret,passwd,getString(R.string.input_second_secret_hint))){
            return;
        }

        if (!passwd.equals(passwd2)){
            AppUtil.toastError(getContext(),getString(R.string.password_inconsistency));
            return;
        }

        presenter.storeSecondPassword(passwd,getArguments().getString("password"));
        showHUD();

    }

    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
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
    public void setPresenter(SecondSecretContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayError(Throwable exception) {
        dismissHUD();
        AppUtil.toastError(getContext(),AppUtil.extractInfoFromError(getContext(),exception));
    }


    @Override
    public void displaySetSecondSecretResult(boolean success, String msg) {
        if (getActivity()==null)
            return;
        dismissHUD();
        if (success) {

            AppUtil.toastSuccess(getContext(), msg);
            Intent intent = new Intent(getActivity(), SecureSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else {
            AppUtil.toastError(getContext(),msg);
        }
    }

}
