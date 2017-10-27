package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import butterknife.BindView;

/**
 * Created by kimziv on 2017/10/27.
 */

public class TransactionDetailFragment extends BaseFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_transaction_detail,container,false);


        return rootView;
    }
}
