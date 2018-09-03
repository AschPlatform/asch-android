package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import asch.so.base.fragment.BaseDialogFragment;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.activity.AccountCreateActivity;
import asch.so.wallet.activity.AccountImportActivity;
import asch.so.wallet.contract.AccountsContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountsPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.AccountsAdapter;
import asch.so.wallet.view.widget.InputPasswdDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 * 所有账户
 */

public class AccountsFragment extends BaseFragment implements AccountsContract.View{
    private static final String TAG=AccountsFragment.class.getSimpleName();
    private final int FLAG_ADD_ACCOUNT = 0;
    private final int FLAG_IMPORT_ACCOUNT = 1;
    private final int FLAG_DEL_ACCOUNT = 2;
    @BindView(R.id.accounts_rcv)
    RecyclerView accountsRecycleView;
    private AccountsAdapter accountsAdapter;

    private Unbinder unbinder;
    private AccountsContract.Presenter presenter;



    public static AccountsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountsFragment fragment = new AccountsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_accounts, container, false);
        unbinder= ButterKnife.bind(this,rootView);
        Context ctx=rootView.getContext();

        accountsRecycleView.setLayoutManager(new LinearLayoutManager(ctx));
        accountsRecycleView.setItemAnimator(new DefaultItemAnimator());
        accountsRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        accountsAdapter=new AccountsAdapter();
        accountsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {

                Account account = (Account) baseQuickAdapter.getItem(position);
                presenter.setCurrentAccount(account);
                accountsAdapter.notifyDataSetChanged();
                getActivity().finish();
            }
        });

        accountsRecycleView.setAdapter(accountsAdapter);
        if (presenter!=null) {
            presenter.subscribe();
        }


        View header=LayoutInflater.from(getContext()).inflate(R.layout.footer_accounts,accountsRecycleView,false);
        header.findViewById(R.id.create_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkPwd(FLAG_ADD_ACCOUNT);
            }
        });
        header.findViewById(R.id.import_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPwd(FLAG_IMPORT_ACCOUNT);
            }
        });
        accountsAdapter.addFooterView(header);
        presenter=new AccountsPresenter(getContext(),this);
        presenter.loadSavedAccounts();

        return rootView;
    }

    
    private void checkPwd(int Flag){
        InputPasswdDialog dialog = InputPasswdDialog.newInstance();
        dialog.setOnClickListener(new BaseDialogFragment.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog, int which) {
                EditText editText = dialog.getDialog().findViewById(R.id.passwd_et);
                String inputPasswd=editText.getText().toString().trim();
                if(Wallet.getInstance().checkPassword(inputPasswd)){
                    operateAccount(Flag,dialog);
                }else {
                    AppUtil.toastError(getActivity(),getString(R.string.password_error));
                }
            }
        });
        dialog.setOnCancelListener(new BaseDialogFragment.OnCancelListener() {
            @Override
            public void onCancel(BaseDialogFragment dialog) {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(),"operate_account");
    }

    private void operateAccount(int Flag,BaseDialogFragment dialog){
        if(Flag == FLAG_DEL_ACCOUNT){

            if (AccountsManager.getInstance().getAccountsCount()<2){
                AppUtil.toastError(getActivity(),getString(R.string.only_one_account));
                return;
            }

            AccountsManager.getInstance().removeCurrentAccount();
            AppUtil.toastSuccess(getActivity(),getString(R.string.delete_success));
            dialog.dismiss();
            return;
        }

        Class<?> clazz = null;
        if(Flag == FLAG_ADD_ACCOUNT){
            clazz = AccountCreateActivity.class;
        }
        if(Flag == FLAG_IMPORT_ACCOUNT){
            clazz = AccountImportActivity.class;
        }

        Intent intent =new Intent(getActivity(),clazz);
        startActivityForResult(intent,1);
        dialog.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unSubscribe();
    }

    @Override
    public void setPresenter(AccountsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {

    }

    @Override
    public void displaySavedAccounts(List<Account> accountList) {
        this.accountsAdapter.replaceData(accountList);
    }


    @Override
    public void gotoCreateAccount() {

    }

    @Override
    public void gotoImportAccount() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.subscribe();
    }
}
