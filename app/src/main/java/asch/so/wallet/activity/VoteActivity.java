package asch.so.wallet.activity;

import android.database.DataSetObservable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import asch.so.wallet.R;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.view.fragment.MyVoteRecordFragment;
import asch.so.wallet.view.fragment.TestFragment;
import asch.so.wallet.view.fragment.VoteDelegatesFragment;
import asch.so.wallet.view.fragment.WhoVoteForMeFragment;
import asch.so.wallet.view.fragment.dummy.DummyContent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteActivity extends TitleToolbarActivity implements VoteDelegatesFragment.OnListFragmentInteractionListener {
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private VoteDelegatesFragment voteDelegatesFragment;
    private  MyVoteRecordFragment myVoteRecordFragment;
    private WhoVoteForMeFragment whoVoteForMeFragment;

    private MyPagerAdapter adapter;

    private ArrayList<Fragment> mFagments = new ArrayList<>();
    private String[] mTitles;// = {getString(R.string.node_list), getString(R.string.vote_record), getString(R.string.vote_me)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.vote));
        mTitles = new String[]{getString(R.string.node_list), getString(R.string.vote_record), getString(R.string.vote_me)};
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected int activityLayoutResId() {
        return R.layout.activity_vote;
    }

    private void initView() {
        voteDelegatesFragment=VoteDelegatesFragment.newInstance(1);
        myVoteRecordFragment=MyVoteRecordFragment.newInstance(1);
        whoVoteForMeFragment=WhoVoteForMeFragment.newInstance(1);
        mFagments.add(voteDelegatesFragment);
        mFagments.add(myVoteRecordFragment);
          mFagments.add(whoVoteForMeFragment);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tablayout.setViewPager(viewPager, mTitles);
        //viewPager.setOffscreenPageLimit(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:{
                        voteDelegatesFragment.refreshData();
                    }
                    break;
                    case  1:{
                        myVoteRecordFragment.refreshData();
                    }
                    break;
                    case 2:{
                        whoVoteForMeFragment.refreshData();
                    }
                    break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onListFragmentInteraction(Delegate item) {

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

//        private DataSetObservable mObservable = new DataSetObservable();
//        public static final int POSITION_UNCHANGED = -1;
//        private  int mChildCount=0;
//        public static final int POSITION_NONE = -2;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
//        }

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

//        @Override
//        public int getItemPosition(@NonNull Object object) {
//                return POSITION_NONE;
//        }

//        @Override
//        public void notifyDataSetChanged() {
//            mChildCount = getCount();
//            super.notifyDataSetChanged();
//        }
    }
}
