package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import asch.so.wallet.R;
import asch.so.wallet.view.fragment.DAppsFragment;
import asch.so.wallet.view.fragment.InstalledDAppsFragment;
import asch.so.wallet.view.fragment.MyVoteRecordFragment;
import asch.so.wallet.view.fragment.VoteDelegatesFragment;
import asch.so.wallet.view.fragment.WhoVoteForMeFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2018/1/19.
 */

public class DAppCenterActivity extends TitleToolbarActivity {
    private static final String TAG=DAppCenterActivity.class.getSimpleName();
    @BindView(R.id.tablayout)
    SegmentTabLayout tablayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private MyPagerAdapter adapter;
    private DAppsFragment dAppsFragment;
    private InstalledDAppsFragment installedDAppsFragment;
    private ArrayList<Fragment> mFagments = new ArrayList<>();
    private String[] mTitles;


    @Override
    protected int activityLayoutResId() {
        return R.layout.activity_dapp_center;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.discovery));
        mTitles = new String[]{getString(R.string.dapps_list), getString(R.string.my_dapps)};
        ButterKnife.bind(this);

        initView();
    }


    private void initView() {
        dAppsFragment=DAppsFragment.newInstance();
        installedDAppsFragment=InstalledDAppsFragment.newInstance();

        mFagments.add(dAppsFragment);
        mFagments.add(installedDAppsFragment);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
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
