package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;

/**
 * Created by kimziv on 2017/10/17.
 */

public class AccountBackupFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView =inflater.inflate(R.layout.fragment_account_backup,container,false);

        return rootView;
    }
}
