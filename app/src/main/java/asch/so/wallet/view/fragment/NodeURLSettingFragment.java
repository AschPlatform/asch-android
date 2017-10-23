package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/23.
 */

public class NodeURLSettingFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.url_et)
    TextView urltEt;
    @BindView(R.id.default_url_btn)
    Button defaultURLBtn;

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
        defaultURLBtn.setOnClickListener(this);
        setDefaultURL();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view==defaultURLBtn){
            setDefaultURL();
        }
    }
    private void setDefaultURL(){
        urltEt.setText(AppConstants.DEFAULT_NODE_URL);
    }
}
