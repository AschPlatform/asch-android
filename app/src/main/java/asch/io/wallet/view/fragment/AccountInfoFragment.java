package asch.io.wallet.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;

import asch.io.base.activity.BaseActivity;
import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.activity.LockCoinsActivity;
import asch.io.wallet.activity.SecondSecretActivity;
import asch.io.wallet.contract.AccountInfoContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.presenter.AccountInfoPresenter;
import asch.io.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountInfoFragment extends BaseFragment implements AccountInfoContract.View, View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    @BindView(R.id.balance_tv)
    TextView balanceTv;
    @BindView(R.id.pubkey_tv)
    TextView pubkeyTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.second_passwd_btn)
    TextView secondPasswdBtn;
    @BindView(R.id.lock_coins_btn)
    TextView lockCoinsBtn;
    @BindView(R.id.tv_lock_date)
    TextView tv_lock_date;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private AccountInfoContract.Presenter presenter;

    public AccountInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountInfoFragment newInstance(String param1, String param2) {
        AccountInfoFragment fragment = new AccountInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        presenter=new AccountInfoPresenter(getContext(),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_info, container, false);
        ButterKnife.bind(this,rootView);
        secondPasswdBtn.setOnClickListener(this);
        lockCoinsBtn.setOnClickListener(this);
        addressTv.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadAccountInfo();
    }

    @Override
    public void onClick(View v) {
        if (v==secondPasswdBtn){
            BaseActivity.start(getActivity(),SecondSecretActivity.class,null);
        }else  if (v==lockCoinsBtn){
            BaseActivity.start(getActivity(),LockCoinsActivity.class,null);
        }else if (v==addressTv){
            copyAddress();
        }
    }

    private void copyAddress(){
        String address=addressTv.getText().toString().trim();
        if (!TextUtils.isEmpty(address)){
            AppUtil.copyText(getContext(),address);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setPresenter(AccountInfoContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(Throwable exception) {

    }

    @Override
    public void dispLockInfo(String lockeAmount, String lockedDate) {
        tv_lock_date.setText(getString(R.string.locked_amount_colon)+lockeAmount+" XAS\n"+getString(R.string.locked_deadline_colon)+lockedDate);
    }

    @Override
    public void displayAccountInfo(Account account) {
        setAccountInfo(account);
    }

    private void setAccountInfo(Account account){
        if (account.getFullAccount()!=null && account.getFullAccount().getAccount()!=null){
            BigDecimal decimal=account.getFullAccount().getAccount().getBalanceDecimalValue();
            balanceTv.setText(String.format("%s XAS",AppUtil.decimalFormat(decimal)));
        }else {
            balanceTv.setText(String.format("%d XAS",0));
        }

        pubkeyTv.setText(account.getPublicKey());
        addressTv.setText(account.getAddress());
        boolean isSetSecondPasswd=account.hasSecondSecret();
        secondPasswdBtn.setText(isSetSecondPasswd?getString(R.string.have_set):getString(R.string.not_set));
        secondPasswdBtn.setEnabled(!isSetSecondPasswd);
        boolean isLocked=account.hasLockCoins();
        lockCoinsBtn.setText(isLocked?getString(R.string.have_lock):getString(R.string.not_lock));
    }


    /**
     *
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
