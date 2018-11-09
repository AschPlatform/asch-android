package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.activity.AccountsActivity;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AccountDeleteFragment extends BaseFragment{

    Unbinder unbinder;
    @BindView(R.id.del_next)
    Button nextBtn;
    @BindView(R.id.del_mnemonic)
    EditText mnemonicTv;
    String seed;
    String[] listWords;




    public static AccountDeleteFragment newInstance() {
        Bundle args = new Bundle();
        AccountDeleteFragment fragment = new AccountDeleteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_delete,container,false);
        unbinder= ButterKnife.bind(this,rootView);

        Account account = AccountsManager.getInstance().getDelAccount();
        seed = account.decryptSecret(getArguments().getString("password"));

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seed.equals(mnemonicTv.getText().toString().trim())) {

                    AccountsManager.getInstance().removeAccount(account);
                    AppUtil.toastSuccess(getActivity(),getString(R.string.delete_success));
                    Intent intent = new Intent(getActivity(), AccountsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                }else {
                    AppUtil.toastError(getActivity(),getString(R.string.del_err));
                }
            }
        });


        mnemonicTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                listWords = s.toString().split(" ");
                if (listWords.length==12){
                    nextBtn.setBackground(getResources().getDrawable(R.drawable.btn_round_rect_orange));
                }else{
                    nextBtn.setBackground(getResources().getDrawable(R.drawable.btn_round_rect_gray));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return rootView;
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }


}
