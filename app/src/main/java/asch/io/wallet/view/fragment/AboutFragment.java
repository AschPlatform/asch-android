package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/10/23.
 */

public class AboutFragment extends BaseFragment {


    @BindView(R.id.about_wallet)
    TextView versionTv;
    @BindView(R.id.contact_rl)
    View contact;
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
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] reciver = new String[] { "service@asch.io" };

                Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
                myIntent.setType("plain/text");
                myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
                startActivity(Intent.createChooser(myIntent, ""));
            }
        });
        return rootView;
    }

    private void setVersion(){
        versionTv.setText(String.format("V%s", AppUtils.getAppVersionName()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }


}
