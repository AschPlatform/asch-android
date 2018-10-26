package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/10/23.
 */

public class AboutFragment extends BaseFragment {


    @BindView(R.id.about_wallet)
    TextView versionTv;
    Unbinder unbinder;
    public static AboutFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_about,container,false);
        unbinder = ButterKnife.bind(this,rootView);
        setVersion();
        return rootView;
    }

    private void setVersion(){
        versionTv.setText(String.format("V%s(%d)", AppUtils.getAppVersionName(),AppUtils.getAppVersionCode()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }


}
