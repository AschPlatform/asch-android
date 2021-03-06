package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.AppConfig;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    Unbinder unbinder;

    public static NodeURLSettingFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NodeURLSettingFragment fragment = new NodeURLSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_node_url_setting,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        defaultURLBtn.setOnClickListener(this);
       String  url = AppConfig.getNodeURL();
        if (TextUtils.isEmpty(url))
        {
            setDefaultURL();
        }else {
            urltEt.setText(url);
        }

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_account_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.item_save:
            {
                String url =urltEt.getText().toString().trim();
                if (Validator.check(getContext(), Validator.Type.URL,url,getString(R.string.node_url_error)))
                {
                    if (!url.equals(AppConfig.getNodeURL()))
                    {
                        AppConfig.putNodeURL(url);
                        AppUtil.toastSuccess(getContext(),getString(R.string.save_success));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AppUtil.restartApp(getContext());
                            }
                        },2500);

                    }else {
                        AppUtil.toastSuccess(getContext(),getString(R.string.save_success));
                        getActivity().finish();
                    }


                }
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }

    //    public static void configAschSDK(){
//
//        String url = AppConfig.getNodeURL();
//        AschSDK.Config.initBIP39(Wallet.getInstance().getMnemonicCode());
//        AschSDK.Config.setAschServer(TextUtils.isEmpty(url) ? AppConstants.DEFAULT_NODE_URL : url);
//        if (url.contains("mainnet")){
//            AschSDK.Config.setMagic(AppConstants.MAINNET_MAGIC);
//        }else {
//            AschSDK.Config.setMagic(AppConstants.TESTNET_MAGIC);
//        }
//    }
}
