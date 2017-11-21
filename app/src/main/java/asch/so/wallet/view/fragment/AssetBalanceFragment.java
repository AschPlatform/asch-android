package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.UIException;
import asch.so.wallet.R;
import asch.so.wallet.activity.AccountDetailActivity;
import asch.so.wallet.activity.AssetReceiveActivity;
import asch.so.wallet.activity.AssetTransactionsActivity;
import asch.so.wallet.activity.QRCodeScanActivity;
import asch.so.wallet.activity.TransactionsActivity;
import asch.so.wallet.contract.AssetBalanceContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.presenter.AssetBalancePresenter;
import asch.so.wallet.util.IdenticonGenerator;
import asch.so.wallet.util.StatusBarUtil;
import asch.so.wallet.view.adapter.AssetsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AssetBalanceFragment extends BaseFragment implements AssetBalanceContract.View, View.OnClickListener {
    private static final String TAG = AssetBalanceFragment.class.getSimpleName();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.assets_rcv)
    RecyclerView assetsRcv;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    //    @BindView(R.id.fab)
//    FloatingActionButton floatingActionButton;
    @BindView(R.id.balance_bbl)
    ButtonBarLayout balanceBbl;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.parallax)
    ImageView parallax;

    @BindView(R.id.xas_balance_tv)
    TextView xasBalanceTv;

    @BindView(R.id.ident_icon)
    ImageView identicon;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.backup_btn)
    Button backupBtn;

    @BindView(R.id.add_icon)
    ImageView addIconIv;
    @BindView(R.id.add_btn)
    ImageView addBtn;
    @BindView(R.id.top_balance_tv)
    TextView topBalanceTv;

    EasyPopup moreEasyPopup;

    private Balance accountBalance=null;


    Unbinder unbinder;


    private List<Balance> assetList = new ArrayList<>();
    private AssetsAdapter adapter = new AssetsAdapter(assetList);

    private AssetBalanceContract.Presenter presenter;

    public static AssetBalanceFragment newInstance() {

        Bundle args = new Bundle();

        AssetBalanceFragment fragment = new AssetBalanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_asset_balances, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        assetsRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        assetsRcv.setItemAnimator(new DefaultItemAnimator());
        assetsRcv.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        assetsRcv.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
                Balance balance = assetList.get(position);
                Bundle bundle = new Bundle();
                String json = JSON.toJSONString(balance);
                bundle.putString("balance", json);
                BaseActivity.start(getActivity(), AssetTransactionsActivity.class, bundle);
            }
        });

        identicon.setOnClickListener(this);
        nameTv.setOnClickListener(this);
        addIconIv.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        backupBtn.setOnClickListener(this);
        presenter = new AssetBalancePresenter(this);

        setupRefreshLayout();

        initPopupMenu();

        //StatusBarUtil.immersive(getActivity());
        presenter.loadAccount();
        return rootView;
    }


    private void setupRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                try {
//                    presenter.loadAccount();
                    presenter.loadAssets();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                // TODO: 2017/10/13
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean misAppbarExpand = true;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int scrollRange = appBarLayout.getTotalScrollRange();
                Log.v(TAG, "verticalOffset:" + verticalOffset + ", scrollRange:" + scrollRange);
                float fraction = 1f * (scrollRange + verticalOffset) / scrollRange;
                toolbar.setAlpha((1 - fraction));

                if (fraction < 0.1 && misAppbarExpand) {
                    misAppbarExpand = false;
                    //addIconIv.setAlpha(1.0f);
                    topBalanceTv.setAlpha(1.0f);
                    addIconIv.setClickable(true);
                   // toolbar.setVisibility(View.VISIBLE);
                }

                if (fraction > 0.8 && !misAppbarExpand) {
                    misAppbarExpand = true;
                    // addIconIv.setAlpha(0);
                    topBalanceTv.setAlpha(0);
                    addIconIv.setClickable(false);
                   // toolbar.setVisibility(View.GONE);
                }
            }
        });
        refreshLayout.autoRefresh();

    }

    @Override
    public void onClick(View view) {
        if (view == addIconIv) {
            showPopupMenu(view,SizeUtils.dp2px(30), SizeUtils.dp2px(-2));
        } else if (addBtn == view) {
            showPopupMenu(view,SizeUtils.dp2px(30), SizeUtils.dp2px(-12));
        } else if (backupBtn == view || identicon==view || nameTv==view) {
            if (accountBalance!=null){
                Bundle bundle =new Bundle();
                bundle.putFloat("balance",accountBalance.getRealBalance());
                BaseActivity.start(getActivity(), AccountDetailActivity.class,bundle);
            }
        } else if (view.getId() == R.id.scan_ll) {
            moreEasyPopup.dismiss();
            Bundle bundle = new Bundle();
            bundle.putInt("action", QRCodeScanActivity.Action.ScanAddressToPay.value);
            BaseActivity.start(getActivity(), QRCodeScanActivity.class, bundle);
        } else if (view.getId() == R.id.receive_ll) {
            moreEasyPopup.dismiss();
            Intent intent = new Intent(getActivity(), AssetReceiveActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.transactions_ll) {
            moreEasyPopup.dismiss();
            BaseActivity.start(getActivity(), TransactionsActivity.class, new Bundle());
        }
    }

    private void showPopupMenu(View view, int offsetX, int offsetY) {
        moreEasyPopup.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.LEFT, offsetX,offsetY);
    }

    private void initPopupMenu() {
        moreEasyPopup = new EasyPopup(getContext())
                .setContentView(R.layout.menu_asset_balance)
                .setAnimationStyle(R.style.PopupMenuAnimation)
                .setFocusAndOutsideEnable(true)
//                .setDimValue(0.5f)
//                .setDimColor(Color.RED)
//                .setDimView(mTitleBar)
                .createPopup();
        View contentView = moreEasyPopup.getContentView();
        View scanItem = contentView.findViewById(R.id.scan_ll);
        View receiveItem = contentView.findViewById(R.id.receive_ll);
        View billItem = contentView.findViewById(R.id.transactions_ll);
        scanItem.setOnClickListener(this);
        receiveItem.setOnClickListener(this);
        billItem.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(AssetBalanceContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayAssets(List<Balance> assetList) {
        this.assetList.clear();
        this.assetList.addAll(assetList);
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh(1000);
    }

    @Override
    public void displayXASBalance(Balance balance) {
        String amount = String.valueOf(balance.getRealBalance());
        xasBalanceTv.setText(amount);
        topBalanceTv.setText(amount + " XAS");
        accountBalance=balance;
    }

    @Override
    public void displayAccount(Account account) {
        nameTv.setText(account.getName());
        IdenticonGenerator.getInstance().generateBitmap(account.getAddress(), new IdenticonGenerator.OnIdenticonGeneratorListener() {
            @Override
            public void onIdenticonGenerated(Bitmap bmp) {
                identicon.setImageBitmap(bmp);
            }
        });
    }

    @Override
    public void displayError(UIException ex) {
        Toast.makeText(getContext(),ex==null?"网络错误":ex.getMessage(), Toast.LENGTH_SHORT).show();
        refreshLayout.finishRefresh(1000);
    }
}
