package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;

/**
 * Created by kimziv on 2017/11/7.
 */

public class TodoFragment extends BaseFragment {

    public static TodoFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_todo,container,false);
        return rootView;
    }
}
