package asch.so.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import static android.R.id.widget_frame;

/**
 * @author kimziv (kimziv@qq.com)
 */
public class BaseActivity extends AppCompatActivity {

    /*
     * 解决Vector兼容性问题
     *
     * First up, this functionality was originally released in 23.2.0,
     * but then we found some memory usage and Configuration updating
     * issues so we it removed in 23.3.0. In 23.4.0 (technically a fix
     * release) we’ve re-added the same functionality but behind a flag
     * which you need to manually enable.
     *
     * http://www.jianshu.com/p/e3614e7abc03
     */
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected static final String EXTRA_FRAGMENT = "EXTRA_FRAGMENT";
    private Fragment mFragment;

    //<editor-fold desc="跳转封装">
    private static void start(Context context, Class<?> clazz, Object... params){
        if (context != null && Fragment.class.isAssignableFrom(clazz)) {
            context.startActivity(newIntent(clazz, context, params));
        }
    }

    public static void start(Activity activity, Class<?> clazz, Object... params){
        start((Context) activity, clazz, params);
    }

    public static void start(Fragment fragment, Class<?> clazz, Object... params){
        if (fragment != null) {
            start((Context) fragment.getActivity(), clazz, params);
        }
    }

    private static Intent newIntent(Class<?> clazz, Context context, Object... params) {
        Intent intent = new Intent(context, BaseActivity.class);
        intent.putExtra(EXTRA_FRAGMENT, clazz.getName());
        return intent;
    }
    //</editor-fold>

    //<editor-fold desc=方法">

    //@InjectExtra(value = EXTRA_FRAGMENT,remark = "Fragment类名")
    protected String mFragmentClazz = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentClazz = getIntent().getStringExtra(EXTRA_FRAGMENT);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(widget_frame);
        setContentView(frameLayout);
        replaceFragment();
    }

    protected void replaceFragment() {
        try {
            mFragment = (Fragment) getFragmentClass().newInstance();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(widget_frame, mFragment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
//            AfExceptionHandler.handle(e, "AfFragmentActivity Fragment 类型错误：" + mFragmentClazz);
        }
    }

    //<editor-fold desc="反射缓存">
    private static Map<String, Class> typeCache = new HashMap<>();
    private Class<?> getFragmentClass() throws ClassNotFoundException {
        Class type = typeCache.get(mFragmentClazz);
        if (type == null) {
            typeCache.put(mFragmentClazz, type = Class.forName(mFragmentClazz));
        }
        return type;
    }
    //</editor-fold>

    public Fragment getFragment() {
        return mFragment;
    }

    //</editor-fold>

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
