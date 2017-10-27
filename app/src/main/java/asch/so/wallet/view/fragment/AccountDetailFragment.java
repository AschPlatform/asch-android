package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.widget.toolbar.BaseToolbar;
import asch.so.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountDetailFragment extends BaseFragment{

    @BindView(R.id.toolbar)
    TitleToolbar toolbar;

    public static AccountDetailFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountDetailFragment fragment = new AccountDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_detail, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

}
