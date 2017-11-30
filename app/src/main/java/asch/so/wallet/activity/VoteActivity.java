package asch.so.wallet.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import asch.so.wallet.R;
import asch.so.wallet.view.fragment.TestFragment;
import asch.so.wallet.view.fragment.VoteDelegatesFragment;
import asch.so.wallet.view.fragment.dummy.DummyContent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteActivity extends TitleToolbarActivity implements VoteDelegatesFragment.OnListFragmentInteractionListener {
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private MyPagerAdapter adapter;

    private ArrayList<Fragment> mFagments = new ArrayList<>();
    private String[] mTitles = {"受托人列表", "投票记录", "谁投了我"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("投票");
        ButterKnife.bind(this);

        initView();
       // setContentView(R.layout.activity_vote);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected int activityLayoutResId() {
        return R.layout.activity_vote;
    }

    private void initView() {
        for (String s : mTitles) {
            mFagments.add(VoteDelegatesFragment.newInstance(1));
        }
        //getChildFragmentManager() 如果是嵌套在fragment中就要用这个
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tablayout.setViewPager(viewPager, mTitles);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        
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
