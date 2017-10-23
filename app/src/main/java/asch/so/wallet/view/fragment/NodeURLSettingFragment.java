package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/23.
 */

public class NodeURLSettingFragment extends BaseFragment {


    public static NodeURLSettingFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NodeURLSettingFragment fragment = new NodeURLSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_node_url_setting,container,false);
        ButterKnife.bind(this,rootView);


        return rootView;
    }
}
