package asch.so.wallet.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.contract.AccountInfoContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountInfoPresenter;
import asch.so.wallet.util.AppUtil;
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account_info, container, false);
        ButterKnife.bind(this,rootView);
        secondPasswdBtn.setOnClickListener(this);
        lockCoinsBtn.setOnClickListener(this);
        presenter.loadAccountInfo();
        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (v==secondPasswdBtn){
            AppUtil.toastInfo(getContext(), secondPasswdBtn.getText().toString());
        }else  if (v==lockCoinsBtn){
            AppUtil.toastInfo(getContext(), lockCoinsBtn.getText().toString());
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
    public void displayAccountInfo(Account account) {
        setAccountInfo(account);
    }

    private void setAccountInfo(Account account){
        BigDecimal decimal=account.getFullAccount().getAccount().getBalanceDecimalValue();
        balanceTv.setText(String.format("%s XAS",decimal.toString()));
        pubkeyTv.setText(account.getPublicKey());
        addressTv.setText(account.getAddress());
        boolean isSetSecondPasswd=account.hasSecondSecret();
        secondPasswdBtn.setText(isSetSecondPasswd?"已设置":"未设置");
        secondPasswdBtn.setEnabled(!isSetSecondPasswd);
        boolean isLocked=account.hasLockCoins();
        lockCoinsBtn.setText(isLocked?"已锁仓":"未锁仓");
        lockCoinsBtn.setEnabled(!isLocked);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
