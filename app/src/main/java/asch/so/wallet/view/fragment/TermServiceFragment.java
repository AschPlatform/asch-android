package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;

/**
 * Created by kimziv on 2017/10/16.
 */

public class TermServiceFragment extends BaseFragment {

    public static TermServiceFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TermServiceFragment fragment = new TermServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_term_service,container,false);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
