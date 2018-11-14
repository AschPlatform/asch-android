package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class EditAccountRemarkFragment extends BaseFragment{


    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.create_btn)
    Button createBtn;
    @BindView(R.id.nickname_tv)
    TextView nicknameTv;
    Unbinder unbinder;



    public static EditAccountRemarkFragment newInstance() {

        Bundle args = new Bundle();
        EditAccountRemarkFragment fragment = new EditAccountRemarkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_edit_account_remark,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        createBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    createAccount();
             }});
        String nickname = AccountsManager.getInstance().getCurrentAccount().getFullAccount().getAccount().getName();
        if (TextUtils.isEmpty(nickname))
            nicknameTv.setText(getString(R.string.not_set));
        else
            nicknameTv.setText(nickname);
        nameEt.setText(AccountsManager.getInstance().getCurrentAccount().getName());
        nameEt.selectAll();

        return rootView;
    }



    /**
     * 创建账户
     */
    private void createAccount(){
        String name=nameEt.getText().toString();

        if (!Validator.check(getContext(), Validator.Type.Name,name,getString(R.string.wallet_name_invalid))) {
            return;
        }
        if (AccountsManager.getInstance().hasAccountForName(name)){
            AppUtil.toastError(getContext(),getString(R.string.wallet_name_exist));
            return;
        }
        AccountsManager.getInstance().updateAccount(name);
        AppUtil.toastSuccess(getActivity(),getString(R.string.opreation_success));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (getActivity()!=null)
                    getActivity().finish();
            }
        },1000);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }






}
