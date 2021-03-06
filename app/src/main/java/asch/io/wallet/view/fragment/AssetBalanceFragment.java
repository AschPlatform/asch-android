package asch.io.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.franmontiel.localechanger.LocaleChanger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;
import java.util.List;

import asch.io.base.activity.BaseActivity;
import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.activity.AssetManageActivity;
import asch.io.wallet.activity.AssetReceiveActivity;
import asch.io.wallet.activity.AssetTransactionsActivity;
import asch.io.wallet.activity.CheckPasswordActivity;
import asch.io.wallet.activity.EditAccountNicknameActivity;
import asch.io.wallet.activity.QRCodeScanActivity;
import asch.io.wallet.contract.AssetBalanceContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.AschAsset;
import asch.io.wallet.presenter.AssetBalancePresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.util.IdenticonGenerator;
import asch.io.wallet.view.adapter.AssetsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    @BindView(R.id.balance_bbl)
    ButtonBarLayout balanceBbl;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.parallax)
    ImageView parallax;
    @BindView(R.id.ident_icon)
    ImageView identicon;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.add_icon)
    ImageView addIconIv;
    @BindView(R.id.top_balance_tv)
    TextView topBalanceTv;
    @BindView(R.id.add_icon_scroll)
    ImageView addIconSrcoll;
    @BindView(R.id.copy_address)
    View copyAddress;
    EasyPopup moreEasyPopup;

    Unbinder unbinder;
    private List<AschAsset> assetList = new ArrayList<>();
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
                Bundle bundle = new Bundle();
                AschAsset asset = assetList.get(position);
                bundle.putString("balance", asset.getName());
                BaseActivity.start(getActivity(), AssetTransactionsActivity.class, bundle);
            }
        });
        addIconSrcoll.setOnClickListener(this);
        identicon.setOnClickListener(this);
        nameTv.setOnClickListener(this);
        addIconIv.setOnClickListener(this);
        copyAddress.setOnClickListener(this);
        presenter = new AssetBalancePresenter(this.getContext(),this);
        setupRefreshLayout();
        initPopupMenu();
        presenter.loadAccount();
        return rootView;
    }


    private void setupRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                try {
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
//                LogUtils.vTag(TAG, "verticalOffset:" + verticalOffset + ", scrollRange:" + scrollRange);
                float fraction = 1f * (scrollRange + verticalOffset) / scrollRange;
                toolbar.setAlpha((1 - fraction));

                if (fraction < 0.1 && misAppbarExpand) {
                    misAppbarExpand = false;
                    topBalanceTv.setAlpha(1.0f);
                    addIconIv.setClickable(true);
                }

                if (fraction > 0.8 && !misAppbarExpand) {
                    misAppbarExpand = true;

                    topBalanceTv.setAlpha(0);
                }
            }
        });
        refreshLayout.autoRefresh();

    }

    @Override
    public void onClick(View view) {
        if (view == addIconIv || view == addIconSrcoll) {
            showPopupMenu(view,SizeUtils.dp2px(30), SizeUtils.dp2px(-2));
        } else if (view.getId() == R.id.scan_ll) {
            moreEasyPopup.dismiss();
            Bundle bundle = new Bundle();
            bundle.putInt("action", QRCodeScanActivity.Action.ScanAddressToPay.value);
            BaseActivity.start(getActivity(), QRCodeScanActivity.class, bundle);
        } else if (view.getId() == R.id.receive_ll) {
            moreEasyPopup.dismiss();
            Intent intent = new Intent(getActivity(), AssetReceiveActivity.class);
            startActivity(intent);
        } else if (view==copyAddress){
            AppUtil.copyText(getActivity(),addressTv.getText().toString());
        } else if(view.getId()==R.id.add_asset_ll){
            moreEasyPopup.dismiss();
            Intent intent = new Intent(getActivity(),AssetManageActivity.class);
            startActivityForResult(intent,1);

        } else if(view.getId()==R.id.import_account_ll){
            moreEasyPopup.dismiss();
            Intent intent = new Intent(getActivity(),CheckPasswordActivity.class);
            intent.putExtra("title",AssetBalanceFragment.class.getSimpleName());
            startActivity(intent);
        }
        else if (view==identicon||view==nameTv){
            Intent intent = new Intent(getActivity(),EditAccountNicknameActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==1){
            presenter.editAssets();
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
                .createPopup();
        View contentView = moreEasyPopup.getContentView();
        View scanItem = contentView.findViewById(R.id.scan_ll);
        View receiveItem = contentView.findViewById(R.id.receive_ll);
        View addItem = contentView.findViewById(R.id.add_asset_ll);
        View importItem = contentView.findViewById(R.id.import_account_ll);
        scanItem.setOnClickListener(this);
        receiveItem.setOnClickListener(this);
        addItem.setOnClickListener(this);
        importItem.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
        presenter.unSubscribe();
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
    public void displayAssets(List<AschAsset> assets) {
        this.assetList.clear();
        this.assetList.addAll(assets);
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh(1000);
    }

    @Override
    public void displayXASBalance(AschAsset balance) {
        String amount =balance.getBalanceString();  // String.valueOf(balance.getRealBalance());
        topBalanceTv.setText(amount + balance.getName());
    }

    @Override
    public void displayAccount(Account account) {
        nameTv.setText(account.getName()==null?"":account.getName());
        addressTv.setText(account.getAddress()==null?"":account.getAddress());
        int width=0;
        if (LocaleChanger.getLocale().getLanguage().contains("zh"))
        {
            width=ConvertUtils.dp2px(45);
        }else{
            width=ConvertUtils.dp2px(100);
        }
        IdenticonGenerator.getInstance().generateBitmap(account.getPublicKey(), new IdenticonGenerator.OnIdenticonGeneratorListener() {
            @Override
            public void onIdenticonGenerated(Bitmap bmp) {
                identicon.setImageBitmap(bmp);
            }
        });
    }

    @Override
    public void displayError(java.lang.Throwable ex) {
       String errToast= AppUtil.extractInfoFromError(getContext(), ex);
        AppUtil.toastError(getContext(),errToast);
        refreshLayout.finishRefresh(1000);
    }
}
