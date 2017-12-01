package asch.so.wallet.view.fragment;

import android.os.Bundle;
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
import android.widget.Toast;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConfig;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.TestData;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.validator.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.AschSDK;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_node_url_setting,container,false);
        ButterKnife.bind(this,rootView);
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
                if (Validator.check(getContext(), Validator.Type.URL,url,"节点URL格式不对"))
                {
                    AppConfig.putNodeURL(url);
                    TestData.configAschSDK();
                    getActivity().finish();
                    AppUtil.toastSuccess(getContext(),"保存成功");
                }
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
