package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import asch.so.base.activity.ActivityStackManager;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.TestData;
import asch.so.wallet.activity.FirstStartActivity;
import asch.so.wallet.activity.MainTabActivity;
import asch.so.wallet.contract.AccountCreateContract;
import asch.so.wallet.model.entity.Account;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountCreateFragment extends BaseFragment implements AccountCreateContract.View{

    Unbinder unbinder;

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

    @BindView(R.id.create_btn)
    Button createBtn;

    private AccountCreateContract.Presenter presenter;

    public static AccountCreateFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountCreateFragment fragment = new AccountCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_create,container,false);
        unbinder= ButterKnife.bind(this,rootView);
         createBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    createAccount();
                   if (getArguments()!=null && getArguments().getString("clazz").equals(FirstStartActivity.class.getName())){
                       Intent intent =new Intent(getActivity(), MainTabActivity.class);
                       startActivity(intent);
                       ActivityStackManager.getInstance().finishAll();
                   }else {
                       getActivity().finish();
                   }
             }
         });

        this.presenter.generateSeed();
        return rootView;
    }


    /**
     * 创建账户
     */
    private void createAccount(){
        String seed=seedEt.getText().toString().trim();
        String name=nameEt.getText().toString();
        String passwd=passwdEt.getText().toString();
        String passwd2=passwdEt2.getText().toString();
        String hint=hintEt.getText().toString();

        presenter.storeAccount(seed,name,passwd,hint);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_account_create,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.item_refresh:
//            {
//                this.presenter.generateSeed();
//            }
//            break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void setPresenter(AccountCreateContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void resetSeed(String seed) {

        seedEt.setText(seed);
    }
}
