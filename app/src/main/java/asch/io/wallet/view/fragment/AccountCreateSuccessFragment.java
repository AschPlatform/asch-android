package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;

/**
 * Created by kimziv on 2017/10/17.
 */

public class AccountCreateSuccessFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_create_success,container,false);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
