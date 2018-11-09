package asch.io.wallet.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import asch.io.wallet.R;
import asch.io.wallet.model.entity.Delegate;
import asch.io.wallet.view.fragment.VoteConfirmationFragment.OnListFragmentInteractionListener;
import asch.io.wallet.view.fragment.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class VoteConfirmationAdapter extends BaseQuickAdapter<Delegate,VoteConfirmationAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    public VoteConfirmationAdapter(List<Delegate> mValues, OnListFragmentInteractionListener mListener) {
        super(R.layout.item_voteconfirmation,mValues);
        this.mListener = mListener;
    }

    @Override
    protected void convert(ViewHolder holder, Delegate item) {
        holder.mIdView.setText(item.getUsername());
        holder.mContentView.setText(item.getAddress());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    public class ViewHolder extends BaseViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
