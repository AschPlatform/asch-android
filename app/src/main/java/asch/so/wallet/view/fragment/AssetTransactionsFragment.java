package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.AssetTransferActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransactionsFragment extends BaseFragment {

    @BindView(R.id.goto_transfer_btn)
    Button transferBtn;
    @BindView(R.id.goto_receive_btn)
    Button receiveBtn;

    public static AssetTransactionsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetTransactionsFragment fragment = new AssetTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transactions,container,false);
        ButterKnife.bind(this,rootView);
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();
                Intent intent =new Intent(getActivity(), AssetTransferActivity.class);
                intent.putExtra("curreny",bundle.getString("curreny"));
                intent.putExtra("precision",bundle.getInt("precision"));
                startActivity(intent);
            }
        });

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
    }
}
