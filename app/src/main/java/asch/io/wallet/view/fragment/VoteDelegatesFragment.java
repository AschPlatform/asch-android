package asch.io.wallet.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.VoteDelegatesContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.Delegate;
import asch.io.wallet.presenter.VoteDelegatesPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.adapter.VoteDelegatesAdapter;
import asch.io.wallet.view.widget.AllPasswdsDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VoteDelegatesFragment extends BaseFragment implements VoteDelegatesAdapter.OnSelectedDelegatesListener, View.OnClickListener, VoteDelegatesContract.View{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_ll)
    LoadingLayout loadingLayout;

    @BindView(R.id.vote_btn)
    Button voteBtn;
    @BindView(R.id.status_tv)
    TextView statusTv;

    private AllPasswdsDialog dialog=null;
    private KProgressHUD hud=null;

    private VoteDelegatesContract.Presenter presenter;
    private VoteDelegatesAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VoteDelegatesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static VoteDelegatesFragment newInstance(int columnCount) {
        VoteDelegatesFragment fragment = new VoteDelegatesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote_delegates, container, false);
        ButterKnife.bind(this,view);
        presenter=new VoteDelegatesPresenter(getContext(),this);
        adapter=new VoteDelegatesAdapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
//                Delegate delegate = adapter.getItem(position);
//                String json = JSON.toJSONString(delegate);
//                Bundle bundle=new Bundle();
//                bundle.putString("transaction",json);
//                BaseActivity.start(getActivity(), TransactionDetailActivity.class,bundle);
            }
        });

        // materialHeader = (MaterialHeader)refreshLayout.getRefreshHeader();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.loadFirstPageDelegates();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                presenter.loadMorePageDelegates();
            }
        });
        refreshLayout.autoRefresh();

        voteBtn.setOnClickListener(this);

        return view;
    }
    public void refreshData(){
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter!=null)
        {
            presenter.unSubscribe();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setPresenter(VoteDelegatesContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            AppUtil.toastError(getContext(),AppUtil.extractInfoFromError(getContext(),exception));
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
    public void displayFirstPageDelegates(List<Delegate> delegates) {
        if (delegates.isEmpty()) {
            loadingLayout.showEmpty();
        }else {
            loadingLayout.showContent();
        }
        adapter.replaceData(delegates);
        refreshLayout.finishRefresh(500);
    }

    @Override
    public void displayMorePageDelegates(List<Delegate> delegates) {
        adapter.addData(delegates);
        refreshLayout.finishLoadmore(500);
    }

    @Override
    public void displayVoteResult(boolean success, String msg) {
        dismissHUD();
        if (success){
            adapter.disableVotedDelegates();
            clearSeletedDelegates();
            AppUtil.toastSuccess(getContext(),msg);
            if (dialog!=null){
                dialog.dismiss();
            }
        }else {
            AppUtil.toastError(getContext(),msg);
        }
    }

    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void onClick(View v) {
        if (v==voteBtn){
//            if (!checkFee()){
//                AppUtil.toastError(getContext(),"余额不足");
//                return;
//            }
            List<Delegate> selectedDelegates=adapter.getSelectedDelegates();
            if (selectedDelegates!=null && selectedDelegates.size()>0){
                boolean hasSecondSecret=getAccount().hasSecondSecret();
                dialog = new AllPasswdsDialog(getContext(),hasSecondSecret);
                dialog.setTitle(getContext().getString(R.string.vote_to_trustee));
                dialog.show(new AllPasswdsDialog.OnConfirmationListenner() {
                    @Override
                    public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                        if (TextUtils.isEmpty(errMsg)){
                            voteForDelegates(secret, secondSecret);
                            showHUD();
                        }else {
                            AppUtil.toastError(getContext(), errMsg);
                        }
                    }
                });
            }else {
                AppUtil.toastError(getContext(), getContext().getString(R.string.please_select_delegate));
            }
        }
    }

    private boolean checkFee(){
        Account account= AccountsManager.getInstance().getCurrentAccount();
        if (account.getFullAccount()==null)
            return true;
       return account.getFullAccount().getAccount().checkVoteFee();
    }

    private void voteForDelegates(String secret, String secondSecret){
        LinkedHashMap<String,Delegate> delegatesMap= adapter.getSelectedDelegatesMap();
        Iterator<Map.Entry<String,Delegate>> it=delegatesMap.entrySet().iterator();
        ArrayList<Delegate> delegates=new ArrayList<>();
        while (it.hasNext()){
            Map.Entry<String,Delegate> entry =it.next();
            delegates.add(entry.getValue());
        }
        if (delegates.size()==0){
            AppUtil.toastError(getContext(),getContext().getString(R.string.please_select_delegate));
            return;
        }
        presenter.voteForDelegates(delegates,secret, secondSecret);
    }

    private void showSelectedDelegatesCount(int count){
        statusTv.setText(String.format(getContext().getString(R.string.select_vote_format),count));
    }

    private void clearSeletedDelegates(){
        adapter.clearSelectedDelegatesMap();
        statusTv.setText(String.format(getContext().getString(R.string.select_vote_format),0));
    }

    @Override
    public void selectDelegate(Delegate delegate) {
        int count=  adapter.getSelectedDelegatesMap().size();
        showSelectedDelegatesCount(count);
    }

    @Override
    public void deselectDelegate(Delegate delegate) {
        int count=  adapter.getSelectedDelegatesMap().size();
        showSelectedDelegatesCount(count);
    }

    @Override
    public boolean checkDelegateCount() {
        int count=  adapter.getSelectedDelegatesMap().size();
        if (count>=33){
            AppUtil.toastWarning(getContext(),getContext().getString(R.string.error_vote_ok_count));
            return false;
        }
        return true;
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
        if (hud!=null){
            hud.dismiss();
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Delegate item);

//        void selectDelegate(Delegate delegate);
//
//        void deselectDelegate(Delegate delegate);
    }
}
