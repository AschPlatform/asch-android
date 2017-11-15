package asch.so.wallet.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.omadahealth.lollipin.lib.PinCompatActivity;
import com.github.omadahealth.lollipin.lib.interfaces.LifeCycleInterface;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;

import java.lang.reflect.Field;
import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.activity.BasePinCompatActivity;
import asch.so.base.view.UIException;
import asch.so.wallet.R;
import asch.so.wallet.contract.DappCenterContract;
import asch.so.wallet.contract.MainContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AssetBalancePresenter;
import asch.so.wallet.presenter.DappCenterPresenter;
import asch.so.wallet.presenter.MainPresenter;
import asch.so.wallet.presenter.MinePresenter;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.adapter.TabFragmentPagerAdapter;
import asch.so.wallet.view.fragment.AssetBalanceFragment;
import asch.so.wallet.view.fragment.DappCenterFragment;
import asch.so.wallet.view.fragment.DappFragment;
import asch.so.wallet.view.fragment.MineFragment;
import asch.so.wallet.view.fragment.TestFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by kimziv on 2017/9/21.
 */

public class MainTabActivity extends BasePinCompatActivity implements MainContract.View, EasyPermissions.PermissionCallbacks{
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    private AssetBalancePresenter assetsPresenter;
    private DappCenterPresenter dappCenterPresenter;
    //private MinePresenter minePresenter;
    private MainPresenter mainPresenter;
    private boolean isExit;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    private static LifeCycleInterface mLifeCycleListener;
//    private final BroadcastReceiver mPinCancelledReceiver;

//    public MainTabActivity() {
//        super();
//        mPinCancelledReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                finish();
//            }
//        };
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        StatusBarUtil.immersive(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_assets:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_discovery:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_mine:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        setupViewPager(viewPager);
        mainPresenter=new MainPresenter(this,this);
        mainPresenter.loadFullAccount();
//
    }

    private void setupViewPager(ViewPager viewPager) {
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager());

        AssetBalanceFragment assetsFragment=AssetBalanceFragment.newInstance();
        DappCenterFragment dappCenterFragment=DappCenterFragment.newInstance();
        MineFragment mineFragment=MineFragment.newInstance();


        adapter.addFragment(assetsFragment);
        adapter.addFragment(dappCenterFragment);
        adapter.addFragment(mineFragment);
        viewPager.setAdapter(adapter);

        assetsPresenter =new AssetBalancePresenter(assetsFragment);
        assetsFragment.setPresenter(assetsPresenter);

        dappCenterPresenter=new DappCenterPresenter(this,dappCenterFragment);
        dappCenterPresenter.loadDappList();

//        minePresenter=new MinePresenter(this, mineFragment);
//        mineFragment.setPresenter(minePresenter);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_mine, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch(id){
//            case R.id.item_manage_wallet:
//            {
//                Intent intent = new Intent(this, AccountsActivity.class);
//                startActivity(intent);
//            }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public static void disableShiftMode(BottomNavigationView navigationView) {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }

    };

    private void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }


    /**
     * 请求相机和闪光灯权限
     */
    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }

    @Override
    public void displayError(UIException exception) {

    }

    @Override
    public void displayAccount(Account account) {

    }

//    @Override
//    protected void onResume() {
//        if (mLifeCycleListener != null) {
//            mLifeCycleListener.onActivityResumed(MainTabActivity.this);
//        }
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        if (mLifeCycleListener != null) {
//            mLifeCycleListener.onActivityPaused(MainTabActivity.this);
//        }
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPinCancelledReceiver);
//    }
//
//    public static void setListener(LifeCycleInterface listener) {
//        if (mLifeCycleListener != null) {
//            mLifeCycleListener = null;
//        }
//        mLifeCycleListener = listener;
//    }
//
//    public static void clearListeners() {
//        mLifeCycleListener = null;
//    }
//
//    public static boolean hasListeners() {
//        return (mLifeCycleListener != null);
//    }
}
