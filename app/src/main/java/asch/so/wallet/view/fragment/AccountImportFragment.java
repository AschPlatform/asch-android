package asch.so.wallet.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.contract.AccountImportContract;
import asch.so.wallet.presenter.AccountImportPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountImportFragment extends BaseFragment implements AccountImportContract.View{

    @BindView(R.id.seed_et)
    EditText seedEt;

    @BindView(R.id.name_et)
    EditText nameEt;

    @BindView(R.id.passwd_et)
    EditText passwdEt;

    @BindView(R.id.passwd_et2)
    EditText passwdEt2;

    @BindView(R.id.hint_et)
    EditText hintEt;

    @BindView(R.id.import_btn)
    Button importBtn;

    private Unbinder unbinder;

    private AccountImportPresenter presenter;

    public static AccountImportFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountImportFragment fragment = new AccountImportFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_account_import,container, false);
       unbinder = ButterKnife.bind(this, rootView);
        Context ctx=this.getContext();

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //presenter.subscribe();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(AccountImportPresenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void reset() {

    }

}
