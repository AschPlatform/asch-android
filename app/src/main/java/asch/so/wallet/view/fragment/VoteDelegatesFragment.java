package asch.so.wallet.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.view.Throwable;
import asch.so.wallet.R;
import asch.so.wallet.contract.VoteDelegatesContract;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.presenter.VoteDelegatesPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.VoteDelegatesAdapter;
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
    public void displayVoteResult(String result) {
        AppUtil.toastError(getContext(),result);
    }

    @Override
    public void onClick(View v) {
        if (v==voteBtn){
            List<Delegate> selectedDelegates=adapter.getSelectedDelegates();
            if (selectedDelegates!=null && selectedDelegates.size()>0){
                AllPasswdsDialog dialog = new AllPasswdsDialog(getContext(),true);
                dialog.show();
//                VoteConfirmationFragment fragment =VoteConfirmationFragment.newInstance(selectedDelegates);
//                fragment.show(getFragmentManager(),"投票给受托人");
            }

            //AppUtil.toastInfo(getContext(),"该功能正在开发中，敬请期待！");
//          LinkedHashMap<String,Delegate> delegatesMap= adapter.getSelectedDelegatesMap();
//          Iterator<Map.Entry<String,Delegate>> it=delegatesMap.entrySet().iterator();
//            ArrayList<Delegate> delegates=new ArrayList<>();
//          while (it.hasNext()){
//              Map.Entry<String,Delegate> entry =it.next();
//              delegates.add(entry.getValue());
//          }
//          if (delegates.size()==0){
//              AppUtil.toastError(getContext(),"请选择受托人");
//              return;
//          }
//          presenter.voteForDelegates(delegates);
        }
    }

    private void showSelectedDelegatesCount(){
        int count=  adapter.getSelectedDelegatesMap().size();
        statusTv.setText(String.format("已选择%d位受托人",count));
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
        void onListFragmentInteraction(Delegate item);

//        void selectDelegate(Delegate delegate);
//
//        void deselectDelegate(Delegate delegate);
    }
}
