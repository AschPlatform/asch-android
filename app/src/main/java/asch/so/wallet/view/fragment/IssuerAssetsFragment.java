package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.P;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.activity.IssueAssetActivity;
import asch.so.wallet.activity.IssuerAgreementActivity;
import asch.so.wallet.activity.RegisterIssuerActivity;
import asch.so.wallet.activity.RegisterUiaAssetActivity;
import asch.so.wallet.contract.IssuerAssetsContract;
import asch.so.wallet.model.entity.IssuerAssets;
import asch.so.wallet.presenter.IssuerAssetsPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.IssuerAssetsAdapter;
import asch.so.wallet.view.widget.RegisterIssuerDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ezy.ui.layout.LoadingLayout;

import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by kimziv on 2017/10/23.
 */

public class IssuerAssetsFragment extends BaseFragment implements IssuerAssetsContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView assetsRcv;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;
    @BindView(R.id.issuer_register)
    Button registerBtn;
    KProgressHUD hud;
    private Unbinder unbinder;
    IssuerAssetsContract.Presenter presenter;
    private IssuerAssetsAdapter adapter = new IssuerAssetsAdapter(getActivity());

    public static IssuerAssetsFragment newInstance() {

        Bundle args = new Bundle();
        IssuerAssetsFragment fragment = new IssuerAssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_issuer_assets,container,false);
        unbinder = ButterKnife.bind(this,rootView);
        presenter = new IssuerAssetsPresenter(getActivity(),this);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHUD();
                presenter.loadIsIssuer();
            }
        });
        assetsRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        assetsRcv.setItemAnimator(new DefaultItemAnimator());
        assetsRcv.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        assetsRcv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IssuerAssets assets = (IssuerAssets) adapter.getItem(position);
                String name = assets.getName();
                String max = AppUtil.getStringFromBigAmount(assets.getMaximum(),assets.getPrecision());
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("max",max);
                BaseActivity.start(getActivity(),IssueAssetActivity.class,bundle);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.loadFirstPageRecords();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                presenter.loadMorePageRecords();
            }
        });
        refreshLayout.autoRefresh();
        presenter.loadFirstPageRecords();
        return rootView;
    }


    @Override
    public void displayFirstPageRecords(List<IssuerAssets> records) {
        if (records.isEmpty()) {
            loadingLayout.showEmpty();
        }else {
            loadingLayout.showContent();
        }
        adapter.replaceData(records);
        refreshLayout.finishRefresh(500);
    }

    @Override
    public void displayMorePageRecords(List<IssuerAssets> records) {
        adapter.addData(records);
        refreshLayout.finishLoadmore(500);
    }

    @Override
    public void showRegisterIssuerDialog() {
        dismissHUD();
        RegisterIssuerDialog dialog;
        dialog= new RegisterIssuerDialog(getActivity());
        dialog.setOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title",getString(R.string.register_issuer));
                BaseActivity.start(getActivity(),IssuerAgreementActivity.class,bundle);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void startRegisterAssetsActivity() {
        Bundle bundle  = new Bundle();
        bundle.putString("title",getString(R.string.register_new_asset));
        BaseActivity.start(getActivity(),IssuerAgreementActivity.class,bundle);
        dismissHUD();

    }

    @Override
    public void setPresenter(IssuerAssetsContract.Presenter presenter) {
        this.presenter = presenter ;
    }

    @Override
    public void displayError(Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            AppUtil.toastError(getContext(),exception==null?getString(R.string.net_error):exception.getMessage());
        }
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null)
            unbinder.unbind();
    }


    private  void  showHUD(){
        if (hud==null){
            hud = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .show();
        }
    }


    private  void  dismissHUD(){
        if (hud!=null) {
            hud.dismiss();
            hud=null;
        }
    }


    private void scheduleHUDDismiss() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dismissHUD();
                getActivity().finish();
            }
        }, 200);
    }
}
