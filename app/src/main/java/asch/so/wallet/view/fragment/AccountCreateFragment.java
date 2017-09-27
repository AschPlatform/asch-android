package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.TestData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountCreateFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.create_btn)
    Button createBtn;

    public static AccountCreateFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountCreateFragment fragment = new AccountCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_create,container,false);
        unbinder= ButterKnife.bind(this,rootView);

         createBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                String words = TestData.testGenerateSeeds(getActivity());
                 Toast.makeText(getActivity(),words,Toast.LENGTH_SHORT).show();
             }
         });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
