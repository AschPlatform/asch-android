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
import android.widget.AdapterView;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;

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
        accountsRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        accountList=new ArrayList<>();
        accountsAdapter=new AccountsAdapter();
        accountsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {

                Account account = accountList.get(position);
                presenter.setCurrentAccount(account);
                accountsAdapter.notifyDataSetChanged();
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
                Intent intent =new Intent(getActivity(),AccountCreateActivity.class);
                startActivityForResult(intent,1);
            }
        });
        header.findViewById(R.id.import_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),AccountImportActivity.class);
                startActivityForResult(intent,1);
            }
        });
        accountsAdapter.addFooterView(header);
        presenter=new AccountsPresenter(getContext(),this);
        presenter.loadSavedAccounts();

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
        this.accountsAdapter.replaceData(accountList);
        this.accountList.clear();
        this.accountList.addAll(accountList);
        accountsAdapter.replaceData(accountList);
//        accountsAdapter.notifyDataSetChanged();
    }

    //public void refresh(){
        //presenter.subscribe();
    //}

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
