package asch.so.wallet.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.contract.LockCoinsContract;
import asch.so.wallet.presenter.LockCoinsPresenter;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LockCoinsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LockCoinsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LockCoinsFragment extends BaseFragment implements LockCoinsContract.View, View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.ok_btn)
    Button okBtn;
    @BindView(R.id.block_height_et)
    EditText blockHeightEt;
    @BindView(R.id.account_passwd_et)
    EditText accountPasswordEt;
    @BindView(R.id.second_passwd_et)
    EditText secondSecretEt;

    private OnFragmentInteractionListener mListener;
    private LockCoinsContract.Presenter presenter;

    public LockCoinsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LockCoinsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LockCoinsFragment newInstance(String param1, String param2) {
        LockCoinsFragment fragment = new LockCoinsFragment();
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
        presenter=new LockCoinsPresenter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lock_coins, container, false);
        ButterKnife.bind(this, rootView);
        okBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v==okBtn){
            String height= blockHeightEt.getText().toString().trim();
            presenter.lockCoins(Long.parseLong(height),"spatial slush action typical emerge feature confirm edge game desk orphan burst","123456Aa");
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
    public void setPresenter(LockCoinsContract.Presenter presenter) {

    }

    @Override
    public void displayError(Throwable exception) {
        AppUtil.toastError(getContext(),exception.getMessage());
    }

    @Override
    public void displayLockCoinsResult(boolean success, String msg) {
        AppUtil.toastSuccess(getContext(),msg);
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
