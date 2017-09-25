package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.AccountCreateActivity;
import asch.so.wallet.activity.AccountImportActivity;
import asch.so.wallet.contract.AccountsContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountsPresenter;
import asch.so.wallet.view.adapter.AccountsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountsFragment extends BaseFragment implements AccountsContract.View{
    private static final String TAG=AccountsFragment.class.getSimpleName();

    @BindView(R.id.accounts_rcv)
    RecyclerView accountsRecycleView;

    @BindView(R.id.goto_create_btn)
    Button createBtn;

    @BindView(R.id.goto_import_btn)
    Button importBtn;


    private AccountsAdapter accountsAdapter;

    private Unbinder unbinder;
    private List<Account> accountList;

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
        accountList=new ArrayList<>();
        accountsAdapter=new AccountsAdapter(accountList);
        accountsAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        accountsRecycleView.setAdapter(accountsAdapter);
        presenter.subscribe();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),AccountCreateActivity.class);
                startActivity(intent);
            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),AccountImportActivity.class);
                startActivityForResult(intent,1);
            }
        });

        return rootView;
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
    public void displaySavedAccounts(List<Account> accountList) {
        this.accountList.clear();
        this.accountList.addAll(accountList);
        accountsAdapter.notifyDataSetChanged();
    }

    public void refresh(){
        presenter.subscribe();
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
