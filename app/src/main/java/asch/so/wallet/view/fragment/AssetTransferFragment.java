package asch.so.wallet.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.presenter.BasePresenter;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.TestData;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.activity.QRCodeScanActivity;
import asch.so.wallet.contract.AssetTransferContract;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransferFragment extends BaseFragment implements AssetTransferContract.View{

    AssetTransferContract.Presenter presenter;

    @BindView(R.id.target_et)
    EditText targetEt;
    @BindView(R.id.ammount_et)
    EditText amountEt;
    @BindView(R.id.memo_et)
    EditText memoEt;
    @BindView(R.id.fee_et)
    EditText feeEt;
    @BindView(R.id.transfer_btn)
    Button transferBtn;


    public static AssetTransferFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AssetTransferFragment fragment = new AssetTransferFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_asset_transfer,container,false);
        ButterKnife.bind(this,rootView);


        targetEt.setKeyListener(DigitsKeyListener.getInstance(AppConstants.DIGITS));
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String currency= AppConstants.XAS_NAME;
                String currency= "KIM.KIM";
                String targetAddress= targetEt.getText().toString().trim();
//                long amount=(long)(Float.parseFloat(amountEt.getText().toString().trim())*Math.pow(10,AppConstants.PRECISION));
                long amount=(long)(Float.parseFloat(amountEt.getText().toString().trim())*Math.pow(10,6));

                String message=memoEt.getText().toString();
                String secret=TestData.secret;
                String secondSecret=TestData.secondSecret;
                presenter.transfer(currency,targetAddress,amount,message,secret,secondSecret);
            }
        });

        feeEt.setKeyListener(null);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_asset_transfer,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_scan_qrcode:
            {
                Intent intent =new Intent(getActivity(), QRCodeScanActivity.class);
                startActivityForResult(intent, 11);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(AssetTransferContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayToast(String toast) {
        Toast.makeText(getActivity(),toast,Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    public void setTargetAddress(String address){
        this.targetEt.setText(address);
    }
}
