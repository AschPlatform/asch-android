package asch.so.wallet.view.fragment;

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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.MyVoteRecordContract;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.presenter.MyVoteRecordPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.MyVoteRecordAdapter;
import asch.so.wallet.view.fragment.dummy.DummyContent.DummyItem;
import asch.so.wallet.view.widget.AllPasswdsDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyVoteRecordFragment extends BaseFragment implements MyVoteRecordContract.View, View.OnClickListener, MyVoteRecordAdapter.OnSelectedDelegatesListener {

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
    @BindView(R.id.down_vote_btn)
    Button downVoteBtn;
    @BindView(R.id.status_tv)
    TextView statusTv;

    private AllPasswdsDialog dialog=null;
    private KProgressHUD hud=null;

    MyVoteRecordAdapter adapter;
    MyVoteRecordContract.Presenter presenter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyVoteRecordFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyVoteRecordFragment newInstance(int columnCount) {
        MyVoteRecordFragment fragment = new MyVoteRecordFragment();
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
        View view = inflater.inflate(R.layout.fragment_myvoterecord_list, container, false);
        ButterKnife.bind(this,view);
        downVoteBtn.setOnClickListener(this);
        presenter=new MyVoteRecordPresenter(getContext(),this);
        adapter=new MyVoteRecordAdapter(this);
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
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                presenter.loadMorePageDelegates();
//            }
//        });
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.autoRefresh();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter!=null){
            presenter.unSubscribe();
        }
    }

    @Override
    public void onClick(View v) {
        if (v==downVoteBtn){
            List<Delegate> selectedDelegates=adapter.getSelectedDelegates();
            if (selectedDelegates!=null && selectedDelegates.size()>0){
                dialog = new AllPasswdsDialog(getContext(),true);
                dialog.show(new AllPasswdsDialog.OnConfirmationListenner() {
                    @Override
                    public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                        if (TextUtils.isEmpty(errMsg)){
                            downVoteForDelegates(secret, secondSecret);
                            showHUD();
                        }else {
                            AppUtil.toastError(getContext(), errMsg);
                        }
                    }
                });
            }
        }
    }

    private void downVoteForDelegates(String secret, String secondSecret){
        LinkedHashMap<String,Delegate> delegatesMap= adapter.getSelectedDelegatesMap();
        Iterator<Map.Entry<String,Delegate>> it=delegatesMap.entrySet().iterator();
        ArrayList<Delegate> delegates=new ArrayList<>();
        while (it.hasNext()){
            Map.Entry<String,Delegate> entry =it.next();
            delegates.add(entry.getValue());
        }
        if (delegates.size()==0){
            AppUtil.toastError(getContext(),"请选择受托人");
            return;
        }
        presenter.downVoteForDelegates(delegates,secret, secondSecret);
    }

    private void showSelectedDelegatesCount(){
        int count=  adapter.getSelectedDelegatesMap().size();
        statusTv.setText(String.format("已选择%d位受托人",count));
    }

    @Override
    public void setPresenter(MyVoteRecordContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {
        if (adapter.getData().isEmpty()){
            loadingLayout.showError();
        }else {
            AppUtil.toastError(getContext(),exception==null?"网络错误":exception.getMessage());
        }
        if (refreshLayout.isRefreshing()){
            refreshLayout.finishRefresh(500);
        }else {
            refreshLayout.finishLoadmore(500);
        }
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
    public void displayDownVoteResult(boolean success, String msg) {
        dismissHUD();
        if (success){
            adapter.removeVotedDelegates();
            AppUtil.toastSuccess(getContext(),msg);
            if (dialog!=null){
                dialog.dismiss();
            }
        }else {
            AppUtil.toastError(getContext(),msg);
        }
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

    @Override
    public void selectDelegate(Delegate delegate) {
        showSelectedDelegatesCount();
    }

    @Override
    public void deselectDelegate(Delegate delegate) {
        showSelectedDelegatesCount();
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
        void onListFragmentInteraction(DummyItem item);
    }
}
