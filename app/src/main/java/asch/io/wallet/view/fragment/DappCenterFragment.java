package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    Unbinder unbinder;
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
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        toolbar.setBackVisible(false);
        toolbar.setTitle(getString(R.string.Discovery));
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
                if (position==1){
                    installedDAppsFragment.lodaDApps();
                }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }
}
