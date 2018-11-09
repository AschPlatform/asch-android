package asch.io.wallet.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import asch.io.base.fragment.BaseDialogFragment;
import asch.io.wallet.R;
import asch.io.wallet.model.entity.Delegate;
import asch.io.wallet.view.adapter.VoteConfirmationAdapter;
import asch.io.wallet.view.fragment.dummy.DummyContent.DummyItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VoteConfirmationFragment extends BaseDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_DELEGATES ="delegates";

    @BindView(R.id.list)
    RecyclerView recyclerView;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private VoteConfirmationAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VoteConfirmationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static VoteConfirmationFragment newInstance(List<Delegate> delegates) {
        VoteConfirmationFragment fragment = new VoteConfirmationFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("delegates", new ArrayList<Delegate>(delegates));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            List<Delegate> delegates = getArguments().getParcelableArrayList(ARG_DELEGATES);
            adapter=new VoteConfirmationAdapter(delegates,null);
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voteconfirmation_list, container, false);
        ButterKnife.bind(this,view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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
