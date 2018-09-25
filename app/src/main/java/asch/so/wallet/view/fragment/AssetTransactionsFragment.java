package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.activity.AssetReceiveActivity;
import asch.so.wallet.activity.AssetTransferActivity;
import asch.so.wallet.contract.AssetTransactionsContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.BaseAsset;
import asch.so.wallet.model.entity.QRCodeURL;
import asch.so.wallet.presenter.AssetTransactionsPresenter;
import asch.so.wallet.presenter.RecordMulitChainPresenter;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
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
    EasyPopup moreEasyPopup;

    String[] pagerTitle;
    private ArrayList<RecordMulitChainFragment> recordsFragments = new ArrayList<>();
    private Balance balance;
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
        balance= (Balance) JSON.parseObject(getArguments().getString("balance"),Balance.class);
        type = balance.getType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asset_transactions,container,false);
        ButterKnife.bind(this,rootView);
        initMultiTab();
        initPopupMenu();
        presenter=new AssetTransactionsPresenter(getActivity(),this);
        transferBtn.setOnClickListener(this);
        receiveBtn.setOnClickListener(this);
        this.displayBalance(balance);
        return rootView;
    }

    @Override
    public void displayBalance(Balance balance) {
        if (type==BaseAsset.TYPE_GATEWAY)
            setHasOptionsMenu(true);
        amountTv.setText(balance.getBalanceString());
        assetTv.setText(balance.getCurrency());
        iconIv.setImageResource(AppUtil.getIconIdByName(balance.getCurrency()));
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

            recordsFragments.add(RecordMulitChainFragment.getInstance(title,balance.getType(),balance.getCurrency(), type));
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
            Balance balance=JSON.parseObject(json,Balance.class);
            QRCodeURL qrCodeURL=new QRCodeURL();
            if (balance!=null){
                qrCodeURL.setAddress("");
                qrCodeURL.setCurrency(balance.getCurrency());
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
            Balance balance=JSON.parseObject(json,Balance.class);
            String currency = balance.getCurrency();
            Bundle params=new Bundle();
            params.putString("currency",currency);
            Intent intent = new Intent(getActivity(), AssetReceiveActivity.class);
            intent.putExtras(params);
            startActivity(intent);
        }
        if (v.getId() == R.id.menu_deposit) {
            moreEasyPopup.dismiss();

        } else if (v.getId() == R.id.menu_withdraw) {
            moreEasyPopup.dismiss();

        }

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
    }

    @Override
    public void setPresenter(AssetTransactionsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(Throwable exception) { }




}
