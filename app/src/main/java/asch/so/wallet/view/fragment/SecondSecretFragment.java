package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kaopiz.kprogresshud.KProgressHUD;

import asch.so.base.activity.ActivityStackManager;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.SecondSecretContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.SecondSecretPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.FeeCalculater;

/**
 * Created by haizeiwang on 2018/1/22.
 */
public class SecondSecretFragment extends BaseFragment implements SecondSecretContract.View {

    private Unbinder unbinder;
    KProgressHUD hud=null;
    private SecondSecretContract.Presenter presenter;
    @BindView(R.id.passwd_et1)
    EditText passwdEt1;
    @BindView(R.id.passwd_et2)
    EditText passwdEt2;
    @BindView(R.id.save_btn)
    Button save_btn;


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

    /**
     * 保存二级密码
     */
    public void saveSecondPassword(){
        String passwd=passwdEt1.getText().toString();
        String passwd2=passwdEt2.getText().toString();
        if (getAccount().getFullAccount()!=null &&
                getAccount().getFullAccount().getBalances()!=null &&
                getAccount().getFullAccount().getBalances().size()!=0 &&
                       getAccount().getXASLongBalance() <= FeeCalculater.calcFee(TransactionType.basic_setPassword)){
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

        if(passwd.contains(" ")) {
            AppUtil.toastError(getContext(),"二级密码不允许设置空格");
             return;
        }

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
            getActivity().finish();
        }else {
            AppUtil.toastError(getContext(),msg);
        }
    }

}
