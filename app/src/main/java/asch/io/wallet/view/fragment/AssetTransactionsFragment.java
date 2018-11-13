package asch.io.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.accounts.AssetManager;
import asch.io.wallet.activity.AssetGatewayDepositActivity;
import asch.io.wallet.activity.AssetReceiveActivity;
import asch.io.wallet.activity.AssetTransferActivity;
import asch.io.wallet.activity.AssetWithdrawActivity;
import asch.io.wallet.activity.CheckPasswordActivity;
import asch.io.wallet.contract.AssetTransactionsContract;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.model.entity.BaseAsset;
import asch.io.wallet.model.entity.QRCodeURL;
import asch.io.wallet.presenter.AssetTransactionsPresenter;
import asch.io.wallet.presenter.RecordMulitChainPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.widget.AlertDialog;
import asch.io.wallet.view.widget.CreateGatewayAccountDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2017/9/27.
 */

public class AssetTransactionsFragment extends BaseFragment implements AssetTransactionsContract.View,View.OnClickListener{

    @BindView(R.id.goto_transfer_btn)
    TextView transferBtn;
    @BindView(R.id.goto_receive_btn)
    TextView receiveBtn;
    @BindView(R.id.ammount_tv)
    TextView amountTv;
    @BindView(R.id.asset_tv)
    TextView assetTv;
    @BindView(R.id.icon_transaction)
    ImageView iconIv;
    View menuAdd;
    @BindView(R.id.tablayout_record)
    SegmentTabLayout tabLayout;
    @BindView(R.id.record_pager)
    ViewPager pager;
    @BindView(R.id.transaction_record_tag)
    View tagRecord;
    @BindView(R.id.can_use_text)
    TextView canUseTv;
    @BindView(R.id.lock_text)
    TextView lockTv;
    @BindView(R.id.view_lock_info)
    LinearLayout lockLl;
    Unbinder unbinder;
    EasyPopup moreEasyPopup;
    KProgressHUD hud;

    String[] pagerTitle;
    private ArrayList<RecordMulitChainFragment> recordsFragments = new ArrayList<>();
    private AschAsset balance;
    private AssetTransactionsContract.Presenter presenter;
    int type;

    public static AssetTransactionsFragment newInstance() {
        Bundle args = new Bundle();
        AssetTransactionsFragment fragment = new AssetTransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        balance= (AschAsset) JSON.parseObject(getArguments().getString("balance"),AschAsset.class);
        type = balance.getType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transactions,container,false);
        unbinder = ButterKnife.bind(this, rootView);
        initMultiTab();
        initPopupMenu();
        presenter=new AssetTransactionsPresenter(getActivity(),this);
        transferBtn.setOnClickListener(this);
        receiveBtn.setOnClickListener(this);


        this.displayBalance(balance);
        return rootView;
    }

    @Override
    public void displayBalance(AschAsset balance) {
        if (type==BaseAsset.TYPE_GATEWAY)
            setHasOptionsMenu(true);
        if (balance.getName().equals(AppConstants.XAS_NAME)){
            lockLl.setVisibility(View.VISIBLE);
            long locked = AccountsManager.getInstance().getCurrentAccount().getFullAccount().getAccount().getLockedAmount();
            String lockStr = locked==0?"0":AppUtil.decimalFormat(AppUtil.decimalFromBigint(locked,AppConstants.PRECISION));
            lockTv.setText(lockStr);
            canUseTv.setText(balance.getBalanceString());
            amountTv.setText(this.balance.getXasTotal());
        }else{
            lockLl.setVisibility(View.GONE);
            amountTv.setText(this.balance==null?"":this.balance.getBalanceString());
        }
        assetTv.setText(balance.getName());
        iconIv.setImageResource(AppUtil.getIconIdByName(balance.getName()));
    }

    private void initMultiTab() {

        if (type== BaseAsset.TYPE_GATEWAY){
            String[] titles = {getString(R.string.record_transfer),getString(R.string.record_deposit),getString(R.string.record_withdraw)};
            pagerTitle = titles;
            tabLayout.setVisibility(View.VISIBLE);
            tagRecord.setVisibility(View.GONE);
        }else {
            String[] titles  = {getString(R.string.record_transfer)};
            pagerTitle = titles;
            tabLayout.setVisibility(View.GONE);
            tagRecord.setVisibility(View.VISIBLE);
        }



        for (String title : pagerTitle) {
            RecordMulitChainPresenter.RecordType type= RecordMulitChainPresenter.RecordType.transfer;

            if (title.equals(getString(R.string.record_transfer)))
                type = RecordMulitChainPresenter.RecordType.transfer;

            else if (title.equals(getString(R.string.record_deposit)))
                type = RecordMulitChainPresenter.RecordType.deposit;

            else if(title.equals(getString(R.string.record_withdraw)))
                type =  RecordMulitChainPresenter.RecordType.withdraw;

            recordsFragments.add(RecordMulitChainFragment.getInstance(title,balance.getType(),balance.getName(), type));
        }

        tabLayout.setTabData(pagerTitle);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                pager.setCurrentItem(i);
            }

            @Override
            public void onTabReselect(int i) {

            }
        });

        pager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setCurrentTab(0);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return recordsFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pagerTitle[position];
        }

        @Override
        public Fragment getItem(int position) {
            return recordsFragments.get(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (v==transferBtn){
            Bundle bundle = getArguments();
            String json =bundle.getString("balance");
            AschAsset balance=JSON.parseObject(json,AschAsset.class);
            QRCodeURL qrCodeURL=new QRCodeURL();
            if (balance!=null){
                qrCodeURL.setAddress("");
                qrCodeURL.setCurrency(balance.getName());
                qrCodeURL.setAmount("");
            }else {
                qrCodeURL.setAddress("");
                qrCodeURL.setCurrency(AschConst.CORE_COIN_NAME);
                qrCodeURL.setAmount("");
            }
            bundle.putString("qrcode_uri",qrCodeURL.encodeQRCodeURL());
            bundle.putInt("action", AssetTransferActivity.Action.AssetBalanceToTransfer.getValue());
            Intent intent =new Intent(getActivity(), AssetTransferActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else if(v==receiveBtn){
            Bundle bundle = getArguments();
            String json =bundle.getString("balance");
            AschAsset balance=JSON.parseObject(json,AschAsset.class);
            String currency = balance.getName();
            Bundle params=new Bundle();
            params.putString("currency",currency);
            Intent intent = new Intent(getActivity(), AssetReceiveActivity.class);
            intent.putExtras(params);
            startActivity(intent);
        }
        if (v.getId() == R.id.menu_deposit) {

            //判断是否开通bch充值地址。-》跳密码-》开户成功,请等开户生效后充值
            presenter.loadGatewayAddress(balance.getGateway(),AccountsManager.getInstance().getCurrentAccount().getAddress());
            moreEasyPopup.dismiss();

        } else if (v.getId() == R.id.menu_withdraw) {
            Bundle bundle = getArguments();
            String json =bundle.getString("balance");
            AschAsset balance=JSON.parseObject(json,AschAsset.class);
            QRCodeURL qrCodeURL=new QRCodeURL();
            if (balance==null){
                AppUtil.toastError(getContext(),getString(R.string.err_get_asset));
                return;
            }
//            qrCodeURL.setAddress("");
            qrCodeURL.setCurrency(balance.getName());
//            qrCodeURL.setAmount("");
            bundle.putString("qrcode_uri",qrCodeURL.encodeQRCodeURL());
            bundle.putInt("action", AssetWithdrawActivity.Action.AssetBalanceToTransfer.getValue());
            Intent intent =new Intent(getActivity(), AssetWithdrawActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            moreEasyPopup.dismiss();

        }

    }

    @Override
    public void showDeposit(String address) {
        dismissHUD();
        Intent intent = new Intent(getActivity(),AssetGatewayDepositActivity.class);
        intent.putExtra("currency",balance.getName());
        intent.putExtra("address",address);
        startActivity(intent);
    }

    @Override
    public void showCreateAccountDialog() {
        CreateGatewayAccountDialog dialog =
                new CreateGatewayAccountDialog(getContext());

        dialog.setContent(String.format(getString(R.string.create_gateway_account_dialog),balance.getName()))
        .setOkOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float xasBalance =  AssetManager.getInstance().queryAschAssetByName(AppConstants.XAS_NAME).getTrueBalance();
                if (xasBalance>(float) 0.1) {
                    Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
                    Bundle bundle = new Bundle();
                    String clazz = AssetTransactionsFragment.class.getSimpleName();
                    bundle.putString("title", clazz);
                    bundle.putString("currency", balance.getName());
                    bundle.putBoolean("hasSecondPwd", hasSecondPasswd());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    dialog.dismiss();
                }else
                    AppUtil.toastError(getActivity(),getString(R.string.account_balance_insufficient));
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==1){
            String secondSecret = data.getStringExtra("secondPwd");
            String password = data.getStringExtra("password");

            if (hasSecondPasswd()&&TextUtils.isEmpty(secondSecret)){
                AppUtil.toastError(getContext(),getString(R.string.error_failed_to_verify_signature));
                return;
            }

            if (TextUtils.isEmpty(password)){
                AppUtil.toastError(getContext(),getString(R.string.error_failed_to_verify_signature));
                return;
            }

            presenter.createGatewayAccount(balance.getGateway(),null,password,hasSecondPasswd()?secondSecret:null);
            showHUD();
        }
    }

    @Override
    public void showCreateSuccessDialog() {
        dismissHUD();
        AlertDialog alertDialog = new AlertDialog(getContext()).setContent(getString(R.string.activate_gateway_address_success));
        alertDialog.show();
    }

    private boolean hasSecondPasswd(){
        return AccountsManager.getInstance().getCurrentAccount().hasSecondSecret();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(type==BaseAsset.TYPE_GATEWAY){
            inflater.inflate(R.menu.menu_add,menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_btn:
            {
                if (menuAdd ==null)
                    menuAdd = getActivity().getWindow().getDecorView().findViewById(R.id.menu_add_btn);

                showPopupMenu(menuAdd, SizeUtils.dp2px(40), SizeUtils.dp2px(-12));
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupMenu(View view, int offsetX, int offsetY) {
        moreEasyPopup.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.LEFT, offsetX,offsetY);
    }

    private void initPopupMenu() {
        moreEasyPopup = new EasyPopup(getActivity())
                .setContentView(R.layout.menu_mulit_chain)
                .setAnimationStyle(R.style.PopupMenuAnimation)
                .setFocusAndOutsideEnable(true)
                .createPopup();
        View contentView = moreEasyPopup.getContentView();
        View deposit = contentView.findViewById(R.id.menu_deposit);
        View withdraw = contentView.findViewById(R.id.menu_withdraw);

        deposit.setOnClickListener(this);
        withdraw.setOnClickListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void setPresenter(AssetTransactionsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(Throwable exception) {
        dismissHUD();
        AppUtil.toastError(getContext(),exception!=null?exception.getMessage():getString(R.string.net_error));
    }

    private  void  showHUD(){
        if (hud==null){
            hud = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .show();
        }
    }
    private  void  dismissHUD(){
        if (hud!=null) {
            hud.dismiss();
            hud=null;
        }
    }

    private void scheduleHUDDismiss() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dismissHUD();
                getActivity().finish();
            }
        }, 200);
    }



}
