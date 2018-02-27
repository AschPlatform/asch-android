package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.AccountInfoActivity;
import asch.so.wallet.activity.BaseCordovaActivity;
import asch.so.wallet.activity.DAppCenterActivity;
import asch.so.wallet.activity.DappActivity;
import asch.so.wallet.activity.LockCoinsActivity;
import asch.so.wallet.activity.PeersActivity;
import asch.so.wallet.activity.VoteActivity;
import asch.so.wallet.contract.DappCenterContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.model.entity.FullAccount;
import asch.so.wallet.presenter.DappCenterPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.DappsCenterAdapter;
import asch.so.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by kimziv on 2017/10/11.
 */

public class DappCenterFragment extends BaseFragment{
    private static final String TAG = DappCenterFragment.class.getSimpleName();

    @BindView(R.id.toolbar)
    TitleToolbar toolbar;
    @BindView(R.id.tablayout)
    SegmentTabLayout tablayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private  MyPagerAdapter adapter;
    private DAppsFragment dAppsFragment;
    private InstalledDAppsFragment installedDAppsFragment;
    private ArrayList<Fragment> mFagments = new ArrayList<>();
    private String[] mTitles;

    public static DappCenterFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DappCenterFragment fragment = new DappCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(getString(R.string.dapps));
        mTitles = new String[]{getString(R.string.dapps_list), getString(R.string.my_dapps)};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_dapp_center,container,false);
        ButterKnife.bind(this,rootView);
        initView();
        return rootView;
    }

    private void initView() {
        toolbar.setBackVisible(false);
        toolbar.setTitle(getString(R.string.dapps));
        dAppsFragment=DAppsFragment.newInstance();
        installedDAppsFragment=InstalledDAppsFragment.newInstance();

        mFagments.add(dAppsFragment);
        mFagments.add(installedDAppsFragment);
        adapter = new  MyPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        tablayout.setTabData(mTitles);
//        tablayout.setViewPager(viewPager, mTitles);

        tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        //viewPager.setOffscreenPageLimit(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                switch (position){
                    case 0:{

                    }
                    break;
                    case  1:{
                        // installedDAppsFragment.refreshData();
                    }
                    break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                tablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return mFagments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFagments.get(position);
        }

    }
}
